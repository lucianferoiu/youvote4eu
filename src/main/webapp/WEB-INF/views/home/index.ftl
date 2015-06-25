[#ftl] [#-- use the square brackets syntax to avoid clashes with js templates etc. --]
[@content for="title"]You Vote For Europe[/@content]




<div id="questionsContainer" class="q-cont">
	
	[#list questions as q]
	<div id="q${q.id}" class="q hide hidden">
		<div id="qq${q.id}" class="qq">
			<h3>${q.title}</h3>
			<div class="">
				${q.description}
			</div>
			<div class="v-count">
				${q.votesCount} votes so far...
			</div>
		</div>
	</div>
	[/#list]

</div>

