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


[#if flasher.registration_failure??]
<div class="container">
	<div class="jumbotron">
		<h2 class="text-danger">Registration process has failed</h2>
		
		[#if (flasher.registration_failure=="wrong_params") ]
		<p>There was something wrong with the parameters you provided in the registration form.</p>
		<div>Please make sure of the following:
			<ul>
				<li>Provide a valid email to which you have access to (a validation email will be sent there, and you must click the link provided inside).</li>
				<li>Provide a secure password of at least 7 characters long</li>
				<li>Make sure to agree with the <i>Terms of Use</i> by clicking the checkbox</li>
			</ul>
			and <em>try again</em>.
		</div>
		[/#if]
		
		[#if (flasher.registration_failure=="duplicate_email") ]
		<p>The email that you provided is already registered/used.</p>
		<p>If you registered in the past but you don't remember the password, the best course of action is to click the link <a href="#reset-password-modal" data-dismiss="modal" data-toggle="modal" data-target="#reset-password-modal">Forgot your password ?</a> and go reset your password. Otherwise, please try a new and valid email address.</p>
		[/#if]
		
		[#if (flasher.registration_failure=="validation_code_error") ]
		<p>The code you have used to validate your registration is wrong.</p>
		[/#if]

		[#if (flasher.registration_failure=="validation_error") ]
		<p>The validation code is wrong or has expired.</p>
		[/#if]

	</div>
</div>
[/#if]


[#if flasher.signin_failure??]
<div class="container">
	<div class="jumbotron">
		<h2 class="text-danger">Signing in failure</h2>
		
		[#if (flasher.signin_failure=="wrong_params") ]
		<p>You tried to login but failed to provide the right identification credentials.</p>
		<p>If you don't remember the password, the best course of action is to click the link <a href="#reset-password-modal" data-dismiss="modal" data-toggle="modal" data-target="#reset-password-modal">Forgot your password ?</a> and go reset your password.</p>
		[/#if]
		
		[#if (flasher.signin_failure=="too_many_attempts") ]
		<p>You tried to login but failed to provide the right password for too many times in a row.</p>
		<p>If you don't remember the password, the best course of action is to click the link <a href="#reset-password-modal" data-dismiss="modal" data-toggle="modal" data-target="#reset-password-modal">Forgot your password ?</a> and go reset your password.</p>
		[/#if]
		
	</div>
</div>
[/#if]

[#if flasher.pwd_reset_failure??]
<div class="container">
	<div class="jumbotron">
		<h2 class="text-danger">Failed to reset your password</h2>
		
		[#if (flasher.pwd_reset_failure=="wrong_params") ]
		<p>The values you entered in the form were not correct... Please try again.</p>
		[/#if]
		
		[#if (flasher.pwd_reset_failure=="nonexisting") ]
		<p>The email that you provided doesn't belong to a registered Partner...</p>
		[/#if]

		[#if (flasher.pwd_reset_failure=="too_many_attempts") ]
		<p>You tried to reset your password but failed to provide the right email for too many times in a row.</p>
		<p>Perhaps it would be a good idea to register again - unless you were not fishing for email addresses, in which case...</p>
		[/#if]
	</div>
</div>
[/#if]


[#-- ========================================================================================================= --]

[#if flasher.success_message??]
<div class="container">
	<div class="jumbotron">
		
		[#if (flasher.success_message=="registration_successful") ]
		<h2 class="text-success">Registration successful!</h2>
		<p>Thank you for registering!</p>
		<p>But there's one more thing to do: <em>verification of your email</em>.</p>
		<p>Please check your email account (including the Spam/Junk bins) for the email that we sent you. Then click on the validation link there (or paste it in the address bar of your browser) in order to verify that the email address is correct and it is indeed yours.</p>
		[/#if]

		[#if (flasher.success_message=="verification_successful") ]
		<h2 class="text-success">Email verification successful!</h2>
		<p>Congratulations on your civic spirit and thank you for joining our efforts!</p>
		<p>You are now a registered <em>Platform Partner</em> and as such you may propose questions, help in the translation of the questions and support the questions proposed by other Partners that you would like to become public.</p>
		<p>Now you may <a href="#sign-in-modal" data-dismiss="modal" data-toggle="modal" data-target="#sign-in-modal"><em>Sign in</em></a> and start.</p>
		[/#if]

		[#if (flasher.success_message=="pwd_reset_successful") ]
		<h2 class="text-success">Your password was reset!</h2>
		<p>We have sent you a verification email in which you will find a link needed to confirm the reset.</p>
		<p>Please validate the password reset sometimes in the next 24 hours and then you can <em>Sign in</em> as usual. Thank you.</p>
		[/#if]
		
		[#if (flasher.success_message=="new_pwd_verified") ]
		<h2 class="text-success">Your new password was verified!</h2>
		<p>The successful validation via email of the password means that now you can <a href="#sign-in-modal" data-dismiss="modal" data-toggle="modal" data-target="#sign-in-modal"><em>Sign in</em></a> as usual. Thank you.</p>
		[/#if]
		
	</div>
</div>
[/#if]


[#if flasher.info_message??]
<div class="container-fluid">
	<div class="bg-info">
		[#if (flasher.info_message=="autosignin_successful") ]
		<p>You have successfully signed in! You chose to be signed in automatically next time you visit.</p>
		[/#if]
	</div>
</div>
[/#if]


[/#if] [#-- flasher --]