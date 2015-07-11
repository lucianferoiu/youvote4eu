[#ftl] [#-- use the square brackets syntax to avoid clashes with js templates etc. --]
[@content for="title"]You Vote For Europe[/@content]
[@content for="footer_script"]<script src='${context_path}/app/home.js'></script>[/@content]
[@content for="footer_script"]<script src='${context_path}/app/layout.js'></script>[/@content]
[#include "../other/utils.ftl"]

<div class="nav-buffer">
	&nbsp;
</div>

<!-- questions container: "mobile" rendering -->
<div id="qContainerFlow" class="q-cont q-cont-static container-fluid visible-xs-block">
	[#list questions as q]
	<div id="q${q.id}" class="q" 
		data-q-id="${q.id}" data-q-votes="${q.votesCount}" [#if q.arch] data-q-archived="yes"[/#if]
		data-q-pub-date='${q.publishedOn?string["dd/MM/yyyy HH:mm"]}' [#if q.archivedOn??]data-q-arch-date='${q.archivedOn?string["dd/MM/yyyy HH:mm"]}'[/#if]>
			<div class="q-title">${q.title}</div>
			<div class="q-desc visible-xs-block">
				<div class="col-xs-3">
					<div class="q-votes"><span>${q.votesCount}</span><br/>votes</div>
				</div>
				<blockquote class="col-xs-9">
					[#if q.archivedOn??]<footer>Archived on ${q.archivedOn?string["dd/MM/yyyy HH:mm"]}</footer>[/#if]
					<footer>Published on ${q.publishedOn?string["dd/MM/yyyy HH:mm"]}</footer>
				</blockquote>
				<p class="q-desc-p">${q.description}</p>
			</div>
	</div>
	[/#list]
</div>

<!-- questions container: grid rendering -->
<div id="qContainerGrid" class="q-cont container-fluid hidden-xs">

<div id="questions-grid-carousel" class="carousel slide" data-ride="carousel" data-wrap="false" data-interval="60000" data-keyboard="true">
  <!-- Indicators -->
  <ol class="carousel-indicators" >
    <li data-target="#questions-grid-carousel" style="border-color:#6D000D" data-slide-to="0" class="active"></li>
    <li data-target="#questions-grid-carousel" style="border-color:#6D000D" data-slide-to="1"></li>
    <li data-target="#questions-grid-carousel" style="border-color:#6D000D" data-slide-to="2"></li>
  </ol>

  <!-- Wrapper for slides -->
  <div class="carousel-inner" role="listbox">
    <div class="item active">
		<div class="q-page" style="width:100%; height:800px; position:relative;">
			<div class="q-bg-2" style="position: absolute; top:150px;left:300px;width:150px;height:100px;">
				
			</div>
			<div class="q-bg-4" style="position: absolute; top:400px;left:100px;width:150px;height:100px;">
				
			</div>
			<div class="q-bg-5" style="position: absolute; top:500px;left:150px;width:150px;height:100px;">
				
			</div>
		</div>
    </div>
    <div class="item">
		<div class="q-page" style="width:100%; height:800px; position:relative;">
			<div class="q-bg-1" style="position: absolute; top:100px;left:200px;width:150px;height:100px;">
				
			</div>
			<div class="q-bg-6" style="position: absolute; top:300px;left:150px;width:150px;height:100px;">
				
			</div>
			
		</div>
    </div>
    <div class="item">
		<div class="q-page" style="width:100%; height:800px; position:relative;">
			<div class="q-bg-5" style="position: absolute; top:100px;left:100px;width:150px;height:100px;">
				
			</div>
			<div class="q-bg-3" style="position: absolute; top:200px;left:300px;width:150px;height:100px;">
				
			</div>
			<div class="q-bg-1" style="position: absolute; top:300px;left:500px;width:150px;height:100px;">
				
			</div>
		</div>
    </div>
  </div>

  <!-- Controls -->
  <a class="left carousel-control" href="#questions-grid-carousel" role="button" data-slide="prev" style="width:5%; color:#6D000D;">
    <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
    <span class="sr-only">Previous</span>
  </a>
  <a class="right carousel-control" href="#questions-grid-carousel" role="button" data-slide="next" style="width:5%; color:#6D000D;" >
    <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
    <span class="sr-only">Next</span>
  </a>
</div>
	
</div>


[@content for="footer_script"]
<script type="text/javascript">
(function() {
	var root = this;
}());
</script>
[/@content]