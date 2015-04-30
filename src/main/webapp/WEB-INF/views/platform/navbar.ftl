[#ftl] [#-- use the square brackets syntax to avoid clashes with js templates etc. --]



<nav class="navbar navbar-inverse navbar-fixed-top">
	<div class="container-fluid">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
				<span class="sr-only">Toggle navigation</span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="${context_path}/platform/home">You Vote for Europe - Platform Partners</a>
		</div>
[#if (session.authPartner)??]
		<!-- authenticated partner -->
		<div id="navbar" class="navbar-collapse collapse pull-right">
			<ul class="nav navbar-nav">
				<li class="active"><a href="${context_path}/platform/home">Home</a></li>
				<li><a href="${context_path}/platform/questions">Questions</a></li>
				<li><a href="${context_path}/platform/statistics">Statistics</a></li>				
				<li><a href="${context_path}/platform/poposed_questions">Proposed Questions</a></li>
				<li><a href="${context_path}/platform/partners">Partners</a></li>
				<li class="dropdown">
					<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">Hello ${session.authPartner.email} <span class="caret"></span></a>
						<ul class="dropdown-menu" role="menu">
							<li><a href="${context_path}/platform/home/my_questions">My Questions</a></li>
							<li><a href="${context_path}/platform/home/preferences">Preferences</a></li>
							<li><a href="${context_path}/platform/home/help">Help</a></li>
							<li class="divider"></li>
							<li><a href="${context_path}/platform/home/signout">Sign out</a></li>
						</ul>
					</li>
			</ul>
		</div>
[#else]
		<!-- no partner logged in -->
		<div id="navbar" class="navbar-collapse collapse pull-right">
			<ul class="nav navbar-nav">
				<li>
					<button type="button" class="btn btn-default inverted navbar-btn" data-toggle="modal" data-target="#sign-in-modal">Sign in ...</button>
				</li>
			</ul>
		</div>
[/#if]
	</div>
</nav>

[#include "../platform/flash-messages.ftl"]


[#-- =================== --]
[#if !(session.authPartner)??]

[@content for="footer_script"]
	<script src='${context_path}/js/bsvalidator.min.js'></script>
[/@content]

[#if ((flasher.should_sign_up)=="true") ??]
[@content for="footer_script"]
<script>
$('#sign-in-modal').modal('show');
</script>
[/@content]
[/#if]


<div class="modal fade" role="dialog" id="sign-in-modal" aria-labelledby="sign-in-modal-label" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-body modal-no-gutter">
				<div class="container-fluid modal-no-gutter">

					<div class="panel-group modal-no-gutter" id="sign-in-accordion" role="tablist" aria-multiselectable="true">
						<div class="panel panel-default">
							<div class="panel-heading" role="tab" id="sign-in-registered-header">
								<h4 class="panel-title">
									<a data-toggle="collapse" data-parent="#sign-in-accordion" href="#sign-in-registered" aria-expanded="true" aria-controls="sign-in-registered">Sign in..</a>
								</h4>
							</div>
							<div id="sign-in-registered" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="sign-in-registered-header">
								<div class="panel-body">
									<form class="form-horizontal" action="${context_path}/platform/home/signin" method="post" data-toggle="validator" role="form">
										<div class="form-group">
											<label for="pp-signin-email" class="col-sm-3">Email address</label> 
											<div class="col-sm-9">
												<input id="pp-signin-email" name="pp-signin-email" type="text" class="form-control" placeholder="email@address" required>
											</div>
										</div>
										<div class="form-group">
											<label for="pp-signin-password" class="col-sm-3">Password</label> 
											<div class="col-sm-9">
												<input id="pp-signin-password" name="pp-signin-password" type="password" class="form-control" placeholder="password" required>
											</div>
										</div>
										<div class="checkbox col-sm-offset-3">
											<label class="col-sm-6"><input name="pp-signin-remember" type="checkbox"> Remember me</label>
											<div class="">
												<a href="#reset-password-modal" data-dismiss="modal" data-toggle="modal" data-target="#reset-password-modal">Forgot your password ?</a>
											</div>
										</div>
										<hr/>
										<div class="">
											<button type="submit" class="col-sm-2 btn btn-primary">Sign in</button> 
											<button type="button" class="col-sm-2 btn btn-default pull-right" data-dismiss="modal">Close</button>
										</div>
									</form>
								</div>
							</div>
						</div>
						<div class="panel panel-default">
							<div class="panel-heading" role="tab" id="sign-in-register-header">
								<h4 class="panel-title">
									<a class="collapsed" data-toggle="collapse" data-parent="#sign-in-accordion" href="#sign-in-register" aria-expanded="false" aria-controls="sign-in-register">Not yet registered?</a>
								</h4>
							</div>
							<div id="sign-in-register" class="panel-collapse collapse" role="tabpanel" aria-labelledby="sign-in-register-header">
								<div class="panel-body">
									<form class="form-horizontal" action="${context_path}/platform/home/register" method="post" data-toggle="validator" role="form">
										<div class="form-group">
											<label for="pp-register-email" class="col-sm-4">Your email address</label> 
											<div class="col-sm-8">
												<input id="pp-register-email" name="pp-register-email" type="text" class="form-control input-sm" placeholder="email@address" required>
											</div>	
										</div>
										<div class="form-group">
											<label for="pp-register-password" class="col-sm-4">Type a password</label> 
											<div class="col-sm-8">
												<input id="pp-register-password" name="pp-register-password" type="password" class="form-control input-sm" placeholder="password" required>
											</div>
										</div>
										<div class="form-group">
											<label for="pp-register-captcha" class="col-sm-4">Type the code</label> 
											<div class="col-sm-4">
												TODO captcha
											</div>
											<div class="col-sm-4">
												<input id="pp-register-capthca" name="pp-register-capthca" type="text" class="form-control input-sm" placeholder="captcha">
											</div>
										</div>
										<div class="form-group">
											<div class="checkbox col-sm-offset-4">
												<label><input type="checkbox" name="pp-register-agreement" required>Agree with the <a data-toggle="collapse" href="#terms-of-use" aria-expanded="false" aria-controls="terms-of-use">terms of use</a></label>
											</div>
										</div>
										<div class="collapse" id="terms-of-use">
											<div class="col-sm-12">
												<h4>Terms of Use</h4>
												<p>
													Registration and access to this website is available unhindered and free of charge for as long the website is available, under the following tenets:
												</p>
												<ol>
													<li>The intention of the website is to promote a civilized platform for expressing civic oppinion. Henceforth, offensive, vulgar or hateful language, and aggressive or inciting behaviour will not be tolerated.</li>
													<li>The quality of the content is of utmost priority. Before proposing a question, a partner should try to verify if the question was not already proposed, so duplicates can be avoided. The formulation of the question should be clear, rational and concise, and should provide links to further sources of information on the topic. A proposed question will not be published and subjected to public voting until sufficient translations of it into a number of EU languages and significant support from the other partners is achieved, therefore an active participation is highly encouraged.</li>
													<!-- <li></li> -->
												</ol>
												<p>
													Registration signifies an implicit agreement to the above terms.
												</p>
												<hr/>
											</div>
										</div>
										<hr/>
										<div class="col-sm-12">
											<button type="submit" class="col-sm-2 btn btn-success">Register</button> 
											<button type="button" class="col-sm-2 btn btn-default pull-right" data-dismiss="modal">Close</button>
										</div>
									</form>
								</div>
							</div>
						</div>
					</div>

				</div><!-- modal container -->
			</div><!-- modal body -->
		</div><!-- /.modal-content -->
	</div><!-- /.modal-dialog -->
</div><!-- /.modal -->


<div class="modal fade" role="dialog" id="reset-password-modal" aria-labelledby="reset-password-modal-label" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-body">
				<h4 class="panel-title">Reset Your Password..</h4>
				<hr/>
				<div class="panel-body">
					<form class="form-horizontal" action="${context_path}/platform/home/reset_password" method="post" data-toggle="validator" role="form">
						<div class="form-group">
							<label for="pp-signin-email" class="col-sm-4">Your Email address</label> 
							<div class="col-sm-8">
								<input id="pp-signin-email" name="pp-reset-email" type="text" class="form-control" placeholder="email@address" required>
							</div>
						</div>
						<div class="form-group">
							<label for="pp-signin-password" class="col-sm-4">New Password</label> 
							<div class="col-sm-8">
								<input id="pp-signin-password" name="pp-reset-password" type="password" class="form-control" placeholder="password" required>
							</div>
						</div>
						<hr/>
						<div class="form-group">
							<button type="submit" class="col-sm-3 btn btn-primary">Reset Password</button>
							<button type="button" class="col-sm-2 btn btn-default pull-right" data-dismiss="modal">Close</button>
						</div>
					</form>
				</div>
			</div><!-- modal body -->
		</div><!-- /.modal-content -->
	</div><!-- /.modal-dialog -->
</div><!-- /.modal -->


[/#if]