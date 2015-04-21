[#ftl] [#-- use the square brackets syntax to avoid clashes with js templates etc. --]
[#if flasher??]

[#if flasher.access_denied??]
<div class="container">
	<div class="jumbotron">
		<h2 class="text-danger">The access to the page is restricted</h2>
	
		[#if ((flasher.access_denied=="authentication_required")) ]
			<p>You need to sign in as a validated Platform Partner first.</p>
			<p>If you do not have an account, try registering by clicking the <em>Sign in</em> button in 
				the upper right corner and then the <em>Not yet registered?</em> tab.</p>
		[/#if]
	
		[#if ((flasher.access_denied=="not_authorized")) ]
			<p>You are not authorized to acces that section of the application.</p>
			<p>Please have a look at the <a href="#">Help</a> page to see in which cases your access can be granted and/or contact us.</p>
		[/#if]
	
		[#if ((flasher.access_denied=="missing_cookie")) ]
			<p>You are trying to identify yourself with using the <em>Remember me</em> auto sign-in, but no valid browser cookie was presented.</p>
			<p>Please make sure that your browser has cookie settings enabled and try to sign in again with your email/password and check the <em>Remember me</em> option.</p>
		[/#if]

		[#if ((flasher.access_denied=="wrong_cookie")) ]
			<p>You are trying to identify yourself with using the <em>Remember me</em> auto sign-in, but the cookie is <strong class="text-danger">invalid</strong>!</p>
			<p>Please make sure to register, validate the registration by clicking the link sent to you by email and sign in with your email/password first, before enabling the <em>Remember me</em> option.</p>
		[/#if]

		[#if ((flasher.access_denied=="not_verified")) ]
			<p>You are trying to sign in, but your account is not yet <strong class="text-danger">verified</strong>!</p>
			<p>Please check your email account (including the Spam/Junk bins) for the email that we sent you. Then click on the validation link there (or paste it in the address bar of your browser) in order to verify that the email address is correct and it is indeed yours.</p>
		[/#if]

		[#if ((flasher.access_denied=="account_disabled")) ]
			<p>You are trying to sign in, but your account is <strong class="text-danger">disabled</strong>!</p>
		[/#if]


	</div>
</div>
[/#if]


[#if flasher.info_message??]
<div class="container-fluid">
	<div class="well bg-info">
		[#if (flasher.info_message=="registration_successful") ]
		<p></p>
		[/#if]
	</div>
</div>
[/#if]

[/#if] [#-- flasher --]