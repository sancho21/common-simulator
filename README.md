Introduction
============
Common Simulator is a library to create any simulator which uses field based message such as ISO-8583 message, or properties like message. Once you download common-simulator package, you can start creating a simulator. The main methods in the library are:

* `BaseProcessor.process(Map incomingMsg, List responseTemplates, Sender responseSender)` which is called to handle incoming message so that it can react when a matched message comes. Every time a message come, just call this method. The method will search for matched response template among responseTemplates, and then send the rendered response template using the response sender.
* `BaseProcessor.processRequest(RequestTemplate template, Sender requestSender)` which is manually called; e.g. if you want to send a request to remote host.

Background
==========
Please look at this example. Supposed you have a class which interacts to ISO-8583 remote host via socket, just like the following:

`````java
public class Handler {
	// Called once a message arrived
	public void messageReceived(Iso8583Message msg, Socket socket) {
		// Create and send response message according to the request message received
	}

	// Called when you start the application
	public void start() {
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				// Send an echo message
			}
		};

		new Timer().schedule(task, 0, 1000); // Send echo each 1 second
	}
}
`````

That class is not completed yet and you wish to complete it. So, it can respond and generate messages.

To complete the solution with this library, please use:

`````xml
	<dependencies>
		<dependency>
			<groupId>id.web.michsan.csimulator</groupId>
			<artifactId>common-simulator</artifactId>
			<version>3.2.0</version>
		</dependency>
	</dependencies>
`````

Handling incoming requests
==========================
Handling request messages means you need to find response templates which responds to the messages.

`````java
public class Handler {
	private Processor processor;
	private List<ResponseTemplate> responseTemplates;

	public class Handler() {
		processor = new BaseProcessor();
		// responseTemplates = ...;
	}

	// Called once a message arrived
	public void messageReceived(Iso8583Message msg, Socket socket) {
		// Create and send response message according to the request message received
		processor.process(convertToMapFields(msg), responseTemplates, new Sender() {

			@Override
			public void send(Map<String, String> renderedFields) {
				Iso8583Message res = convertFromMapFields(renderedFields);
				send(res, socket); // Your implementation
			}
		});
	}
	
	...
}
`````

Then, how to load the response templates?

Loading templates
=================
You have to load templates then convert them into response templates.
 
`````java
Properties props = loadPropertiesFile(); // Your implementation
String ruleCodesVar = "rule_codes";
String responseSuffix = "response"
List<ResponseTemplate> responseTemplates = ResponseTemplate.convert(
	new TemplateLoader(props, ruleCodesVar, responseSuffix).load());
`````

Defining rules
==============
The "rule_codes" is a property key which contains comma separated code of response templates. While "response" is an additional identifier of the templates. Therefore your properties file should contains:

`````properties
rule_codes=inquiry, purchase, reversal

# This are the template fields
rule.purchase.response.0=0210
rule.purchase.response.2=<echo>
rule.purchase.response.3=<echo>
`````

The structure of a field is `rule.(template code).(additional identifier).(field)=(response value)` So, in the example config file above, purchase will be the template code, response will be the additional identifier and 0,2,3 will be the field.

Sending outgoing requests
=========================
How to complete the following snippet below?
`````java
// Called when you start your application
public void start() {
	TimerTask task = new TimerTask() {
		@Override
		public void run() {
			// Send an echo message
		}
	};

	new Timer().schedule(task, 0, 1000); // Send echo each 1 second
}
`````

To send echo message which is a request message, you need second important method: `BaseProcessor.processRequest(RequestTemplate template, Sender requestSender)`.

`````java
Properties props = loadPropertiesFile();
String ruleCodesVar = "send_periodically";
String responseSuffix = "request"
List<RequestTemplate> requestTemplates = RequestTemplate.convert(
			new TemplateLoader(props, ruleCodesVar, responseSuffix).load());

// Assuming there is only one request template
RequestTemplate echoTemplate = requestTemplates.get(0);

String periodStr = echoTemplate.getProperties().getProperty("period");
long period = Long.parseLong(periodStr);

TimerTask task = new TimerTask() {

	@Override
	public void run() {
		// Send an echo message
		if (session != null && !session.isClosed())
			processor.processRequest(echoTemplate, createSender(session));
	}

};
periodicTasks.add(task);
new Timer().schedule(task, period, period);
`````

And the message properties file would be:
`````properties
send_periodically=echo

# This are the template fields
# Additional properties that is needed by echo message
rule.echo.period=5000
rule.echo.request.0=0800
rule.echo.request.2=<echo>
rule.echo.request.3=<echo>
`````

Specifying response delay
=========================
There are two ways in specifying response delay. So that, the processor will not respond to the sender for a moment (specified delay):
* **Global delay** using `new BaseProcessor().setResponseDelay(long delayInMiliseconds)`. All responses will be delayed before sending.
* **Local delay** using message configuration. To delay rule purchase in 500ms, you need to specify `rule.purchase.response_delay=500` along with the message file.

`````properties
rule_codes=inquiry, purchase, reversal

# This are the template fields
rule.purchase.response.0=0210
rule.purchase.response.2=<echo>
rule.purchase.response.3=<echo>

# Specify delay only for this message
rule.purchase.response_delay=500
`````
Field syntaxes
==============
This simulator library is quite smart in handling incoming request and sending a request. Actually both request and response have common ways to create their fields.

Constant
--------
To send constant, just put the value there. Defining `rule.purchase.response.userName=Muhammad Ichsan` will render `userName=Muhammad Ichsan`.

Echo
----
Echo means creating response value just like the request value. Defining `rule.purchase.response.11=<echo>` means that when an incoming message gives bit `11=998877`, then the response given by the simulator will be exactly `11=998877`.

You also can echo value from other field of request messages. Defining `rule.purchase.response.37=<echo|f:24>` means that bit 37 will be the same with bit 24 of request message. Please remember that, this only applies to response templates.

Partial echo
------------
What if you send a field which value is "Bali Robert Lax0000040000" to simulator and you wish that your simulator replies "I am Robert Lax"? Also when you send "Bali Nichol Zen0000080000" you wish to get reply "I am Nichol Zen". This simulator library is quite smart in creating response. It can create response values based on original request values.

Calling `rule.purchase.response.48=I am <echo|*,10>` will give you "I am " + echo of original(request) field starting from `<echo>` tag location into 10 characters ahead.

Next, to define the beginning of the request copy, please use `rule.purchase.response.48=<echo|5,6> is my nickname`. With that syntax, given original bit 48 value "Bali Robert Lax0000040000" the response will be "Robert is my nickname"

Next, to echo value from other field of request messages, just give additional information. Calling `rule.purchase.response.63=<echo|5,6|f:48> is my nickname` will set bit 63 value from substring value from bit 48 of the request message. Once again, this only applies to response templates.

Generated values
----------------
Some of the generated value are `<date>` and `<counter>`
`````properties
rule.purchase.response.11=<counter>
rule.purchase.response.12=<date:MMddHHmmss>
rule.purchase.response.48=<rotate:Hello World|Halo Dunia|Ohayou Sekai>
rule.purchase.response.48=<random:Hello World|Halo Dunia|Ohayou Sekai>
`````
* Tag `<counter>` generates incremental integer value for each incoming request.
* Tag `<date>` generates formatted string of date in Java standard way
* Tag `<rotate>` generates value by rotating each value.
* Tag `<random>` generates value by randomizing each value.

Fields using the same tag will share the same value. So, if you have field 11 and 37 defined with <counter>, then both will have the same rendered value (e.g. both are 578).
To override the implementation, you have to implement interface `id.web.michsan.csimulator.Resolver` and register it on `resolver.properties` file on classpath.
`````properties
# Inside resolver.properties file
class=id.myweb.MyResolver
`````

Condition syntaxes
==================
The condition syntax allows us to use parenthesis. Supported operation are `&&` and `||` with negation `!`.
`````properties
rule.register_user.condition=0=="0200" && (5=="impos" || 48=="Robertino Mob") && 61!="Padang"
`````

Supported condition syntaxes:
* Exact: `48 == "Hello World"`
* Regex: `0 ~~ "[0-9]{4}"`
* Substring: `48 %% "ello Wo"`

PS: For version < 3.0.0, replace `5 == "impos"` with `5:impos`.
