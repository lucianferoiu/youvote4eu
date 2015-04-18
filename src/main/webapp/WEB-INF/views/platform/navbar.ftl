[#ftl] [#-- use the square brackets syntax to avoid clashes with js templates etc. --]
<nav class="navbar navbar-inverse navbar-fixed-top">
	<div class="container">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
				<span class="sr-only">Toggle navigation</span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="${context_path}/platform/home">You Vote for Europe - Platform Partners</a>
		</div>
[#if partner??]
		<!-- authenticated partner -->
		<div id="navbar" class="navbar-collapse collapse pull-right">
			<ul class="nav navbar-nav">
				<li class="active"><a href="${context_path}/platform/home">Home</a></li>
				<li><a href="${context_path}/platform/questions">Questions</a></li>
				<li><a href="${context_path}/platform/partners">Partners</a></li>
				<li><a href="${context_path}/platform/partners">Partners</a></li>
				<li><a href="${context_path}/platform/home/signout" class="btn navbar-button">Sign out</a></li>
			</ul>
		</div>
[#else]
		<!-- no partner logged in -->
		<div id="navbar" class="navbar-collapse collapse pull-right">
			<ul class="nav navbar-nav">
				<li>
					<button type="button" class="btn btn-primary navbar-btn" data-toggle="modal" data-target="#sign-in-modal">Sign in ...</button>
				</li>
			</ul>
		</div>
[/#if]
	</div>
</nav>



[#if !partner??]
<div class="modal fade" role="dialog" id="sign-in-modal" aria-labelledby="sign-in-modal-label" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			[#-- <div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
				<h4 class="modal-title" id="sign-in-modal-label">
					Sign in or Register..
				</h4>
			</div> --]
			<div class="modal-body">
				<div class="container-fluid">

					<div class="panel-group" id="sign-in-accordion" role="tablist" aria-multiselectable="true">
						<div class="panel panel-default">
							<div class="panel-heading" role="tab" id="sign-in-registered-header">
								<h4 class="panel-title">
									<a data-toggle="collapse" data-parent="#sign-in-accordion" href="#sign-in-registered" aria-expanded="true" aria-controls="sign-in-registered">Sign in..</a>
								</h4>
							</div>
							<div id="sign-in-registered" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="sign-in-registered-header">
								<div class="panel-body">
									<form class="">
										<div class="form-group">
											<label for="pp-signin-email">Email address</label> <input id="pp-signin-email" type="text" class="form-control input-sm" placeholder="email@address">
										</div>
										<div class="form-group">
											<label for="pp-signin-password">Password</label> <input id="pp-signin-password" type="password" class="form-control input-sm" placeholder="password">
										</div>
										<div class="checkbox">
											<label><input type="checkbox"> Remember me</label> <a class="pull-right" href="${context_path}/platform/home/reset-password">Forgot your password ?</a>
										</div>
										<hr/>
										<div class="">
											<button type="submit" class="btn btn-primary">Sign in</button> <button type="button" class="btn btn-default pull-right" data-dismiss="modal">Close</button>
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
									<form class="">
										<div class="form-group">
											<label for="pp-register-email">Your email address</label> <input id="pp-register-email" type="text" class="form-control input-sm" placeholder="email@address">
										</div>
										<div class="form-group">
											<label for="pp-register-password">Type a password</label> <input id="pp-register-password" type="password" class="form-control input-sm" placeholder="password">
										</div>
										<div class="form-group">
											<label for="pp-register-password2">Re-type the password</label> <input id="pp-register-password" type="password" class="form-control input-sm" placeholder="password">
										</div>
										<div class="checkbox">
											<label><input type="checkbox">Agree with the <a href="#">terms of use</a></label>
										</div>
										<hr/>
										<div class="">
											<button type="submit" class="btn btn-success">Register</button> <button type="button" class="btn btn-default pull-right" data-dismiss="modal">Close</button>
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
[/#if]