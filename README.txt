This is a library to create simulator of peer which uses field base message like ISO-8583.

Features:
	- Response template can work with <echo> and <echo partial>
	- Request template with <counter>, <date:format>, <rotate:value 1|value 2> and <random:value 1|value 2>
    - Response can be delayed

To override value resolver, please override id.web.michsan.csimulator.Resolver and register it inside resolver.properties in your path.

For more information, read on http://sourceforge.net/apps/mediawiki/commonsimulator.

Can run on JVM > Java 5.
