This is a library to create simulator of peer which uses field base message like ISO-8583.

Features:
	- Response template can work with <echo> and <echo partial>
	- Request template with <counter> and <date:format>
    - Response can be delayed

To override value resolver, please override id.web.michsan.csimulator.Resolver and register it inside resolver.properties in your path.

Revision History
================

Nov 16th, 2010
--------------
Features: - Implemented complex condition e.g. f43:Ichsan && (f40:Hello || f50:500)

 