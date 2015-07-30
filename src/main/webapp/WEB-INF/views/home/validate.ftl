[#ftl] [#-- use the square brackets syntax to avoid clashes with js templates etc. --]

<div class="modal fade" role="dialog" id="validate-citizen" aria-labelledby="validate-citizen-label" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-body">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="panel-title">Validate vote</h4>
				<hr/>
				<div class="panel-body">
					<form class="form-horizontal" action="/send-validation-mail" method="post" data-toggle="validator" role="form">
						<div class="form-group">
							<label for="citizen-email" class="col-xs-4">Your Email</label>
							<div class="col-xs-8">
								<div class="input-group">
									<span class="input-group-addon">@</span>
									<input id="citizen-email" name="citizen-email" type="email" class="form-control" required>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label for="citizen-country" class="col-xs-4">Your Country</label> 
							<select class="col-xs-8 selectpicker show-tick" name="citizen-country" data-size="7" data-live-search="true" data-show-subtext="true">
								[#list euCountries as c] <option value="${c.code}" data-content="<img src='/img/blank.gif' class='flag flag-${c.code}'/>&nbsp;<span class='text-muted'>[${c.code?upper_case}]</span>&nbsp;&nbsp;${c.label} " [#if (c.code==guessedCountry)] selected="selected" [/#if] data-subtext="${c.code?upper_case}">${c.label}</option>[/#list]
							</select>
						</div>
						
						<div class="col-xs-12">
							<p  style="color:#990012;">
								Before your votes are counted, we need you to validate your <em>anonymous</em> identity via email.
							</p>
							<p class="hidden-xs">
								Your email <strong>will not be stored</strong>, your privacy is well protected - we need your email just as the least-intrusive measure to avoid people voting multiple times... We know you are passionate about these topics, however a fair vote will serve us better, as a community, in the long run.<br/>
								Just as well, the information about the country is used exclusively for statistics of the votes...
							</p>
							<p>
								Please enter your email address above, choose the EU country that best represents you and click the <em>Send <span class="hidden-xs">Validation</span> Email</em> button below.
							</p>
							<p class="hidden-xs">
								We will sent you an email containing a validation link - <strong>click it</strong> and we're done. You only need to do this the first time (well, technically every time you visit us from a <em>new computer/device</em> or use a different browser). We use a randomly-generated number in a cookie to identify you on subsequent visits - absolutely nothing to tie your personal data to the votes you make here, even if our systems are hacked or seized.
							</p>
							<p>
								To find out more about how we protect your <strong>privacy and anonymity</strong> on our site, please check the <a href="/help#privacy">Help</a> page.
							</p>
							</div>
						</div>
						<hr/>
						<div class="form-group row">
							<button id="citizen-validation-submit" type="submit" class="col-xs-offset-1 col-xs-6 btn btn-danger">Send <span class="hidden-xs">Validation</span> Email</button>
							<button id="citizen-validation-cancel" type="button" class="col-xs-offset-1 col-xs-3 btn btn-default" data-dismiss="modal">Close</button>
						</div>
					</form>

						[#-- <a data-toggle="collapse" href="#terms-of-use2" aria-expanded="false" aria-controls="terms-of-use2">terms of use</a>
						<div class="collapse" id="terms-of-use2">
							<div class="col-sm-12">
								<h4>ss</h4>
								<p>
									sd
								</p>

							</div>
						</div> --]
				</div>
			</div><!-- modal body -->
		</div><!-- /.modal-content -->
	</div><!-- /.modal-dialog -->
</div><!-- /.modal -->



<div class="modal fade" id="validate-citizen-reminder">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title">Validate your email</h4>
      </div>
      <div class="modal-body">
        <p class="text-danger">Before any of your votes are counted, you need to validate your identity via email.</p>
		<p>We really need you to do this so we can limit enthusiastic citizens to vote multiple times..</p>
		<p>We sent you an email at the address you previously indicated. Please check it and click the provided link in there.. You may need to look in the spam folders if it's not in your inbox.
		<p class="text-muted"><br/>As mentioned before, safeguarding your privacy is our priority and your identity is protected: we already forgot your email address for precisely that reason.</p>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Alright, I'll check my email..</button>
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div><!-- /.modal -->



