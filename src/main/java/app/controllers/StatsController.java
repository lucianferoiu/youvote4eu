package app.controllers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.RowListenerAdapter;
import org.javalite.activeweb.annotations.GET;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.base.QuestionsListController;
import app.util.JsonHelper;
import app.util.StringUtils;
import app.util.dto.HCDataPoint;

public class StatsController extends QuestionsListController {
	static final Logger log = LoggerFactory.getLogger(StatsController.class);
	protected static final String QUESTION_VOTES_COUNT_BY_COUNTRY = "SELECT c.country ccode, count(c.id) cnt FROM votes v "
			+ "LEFT JOIN citizens c ON v.citizen_id=c.id " + " WHERE v.validated=true and v.question_id=? " + "GROUP BY country";
	protected static final String QUESTION_TALLY_BY_COUNTRY = "SELECT c.country ccode, avg(v.value) tally FROM votes v "
			+ "LEFT JOIN citizens c ON v.citizen_id=c.id " + " WHERE v.validated=true and v.question_id=? " + "GROUP BY country";

	@GET
	public void votesByCountry() {
		String qIdParam = param("id");
		Long qId = null;
		if (!StringUtils.nullOrEmpty(qIdParam)) {
			qId = Long.decode(qIdParam);
		}

		final List<HCDataPoint<Long>> stats = new ArrayList<HCDataPoint<Long>>();
		Base.find(QUESTION_VOTES_COUNT_BY_COUNTRY, qId).with(new RowListenerAdapter() {
			@Override
			public void onNext(Map<String, Object> row) {
				stats.add(new HCDataPoint<Long>((String) row.get("ccode"), (Long) row.get("cnt")));
			}
		});

		String json = JsonHelper.toListJson(stats);
		respond(json).contentType("application/json").status(200);
	}

	@GET
	public void tallyByCountry() {
		String qIdParam = param("id");
		Long qId = null;
		if (!StringUtils.nullOrEmpty(qIdParam)) {
			qId = Long.decode(qIdParam);
		}

		final List<HCDataPoint<Double>> stats = new ArrayList<HCDataPoint<Double>>();
		Base.find(QUESTION_TALLY_BY_COUNTRY, qId).with(new RowListenerAdapter() {
			@Override
			public void onNext(Map<String, Object> row) {
				stats.add(new HCDataPoint<Double>((String) row.get("ccode"), ((BigDecimal) row.get("tally")).doubleValue()));
			}
		});

		String json = JsonHelper.toListJson(stats);
		respond(json).contentType("application/json").status(200);
	}

}
