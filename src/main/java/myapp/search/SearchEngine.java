package myapp.search;

import myapp.model.Document;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: chris
 * Created: 8/25/13
 */
public class SearchEngine {
    public static final class Constants {
        public static final String INDEX_MYAPP = "myapp";
        public static final String MAPPING_TYPE_DOCS = "docs";
        public static final String FIELD_BODY = "body";
    }

    private final Client esClient;

    public SearchEngine(Client esClient) {
        this.esClient = esClient;
    }

    public List<Document> search(String query) {
        // Note: This very simple implementation only returns the first page of results
        SearchResponse response =
                esClient.prepareSearch(Constants.INDEX_MYAPP)
                .setTypes(Constants.MAPPING_TYPE_DOCS)
                .addField(Constants.FIELD_BODY)
                .setQuery(QueryBuilders.fieldQuery(Constants.FIELD_BODY, query))
                .execute()
                .actionGet();
        return toDocuments(response.getHits());
    }

    private List<Document> toDocuments(SearchHits hits) {
        List<Document> docs = new ArrayList<>();
        for (SearchHit hit : hits) {
            String body = hit.field(Constants.FIELD_BODY).getValue();
            docs.add(new Document(body));
        }
        return docs;
    }
}
