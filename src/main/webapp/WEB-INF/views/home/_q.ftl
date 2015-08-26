
<div id="q${q.id}" class="q"
	data-q-id="${q.id}" data-q-votes="${q.votesCount}" [#if q.arch] data-q-archived="yes"[/#if]
	data-q-pub-date='${q.publishedOn?string["dd/MM/yyyy HH:mm"]}' [#if q.archivedOn??]data-q-arch-date='${q.archivedOn?string["dd/MM/yyyy HH:mm"]}'[/#if]>
	<div class="q-title">${q.title}</div>
	<div class="q-desc">
		<blockquote>
			[#if q.archivedOn??]<footer>Archived on ${q.archivedOn?string["dd/MM/yyyy HH:mm"]}</footer>[/#if]
			<footer>Published on ${q.publishedOn?string["dd/MM/yyyy HH:mm"]}</footer>
		</blockquote>
		<p>${q.description}</p>
	</div>
</div>
