[#ftl] [#-- use the square brackets syntax to avoid clashes with js templates etc. --]

[#--
* Generates a "random" integer between min and max (inclusive)
*
* Note the values this function returns are based on the current
* second the function is called and thus are highly deterministic
* and SHOULD NOT be used for anything other than inconsequential
* purposes, such as picking a random image to display.
--]
[#function rand min max]
	[#local now = .now?long?c /]
	[#local randomNum = _rand + ("0." + now?substring(now?length-1) + now?substring(now?length-2))?number /]
	[#if (randomNum > 1)]
		[#assign _rand = randomNum % 1 /]
	[#else]
		[#assign _rand = randomNum /]
	[/#if]
	[#return (min + ((max - min) * _rand))?round /]
[/#function]
[#assign _rand = 0.36 /]