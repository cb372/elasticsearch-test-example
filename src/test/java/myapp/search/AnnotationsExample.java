package myapp.search;

import com.github.tlrx.elasticsearch.test.annotations.*;
import com.github.tlrx.elasticsearch.test.support.junit.runners.ElasticsearchRunner;
import myapp.model.Document;
import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Author: chris
 * Created: 8/25/13
 */
@RunWith(ElasticsearchRunner.class)
public class AnnotationsExample {

    @ElasticsearchNode
    Node node;

    @ElasticsearchClient
    Client client;

    @Test
    @ElasticsearchIndex(
            indexName = SearchEngine.Constants.INDEX_MYAPP,
            mappings = {
                    @ElasticsearchMapping(
                            typeName = SearchEngine.Constants.MAPPING_TYPE_DOCS,
                            properties =  {
                                    @ElasticsearchMappingField(
                                            name = SearchEngine.Constants.FIELD_BODY,
                                            type = ElasticsearchMappingField.Types.String)
                            }
                    )
            }
    )
    @ElasticsearchBulkRequest(dataFile = "data.json")
    public void returnsDocumentsThatMatchQuery() {
        SearchEngine searchEngine = new SearchEngine(client);
        List<Document> docs = searchEngine.search("hello");

        assertThat(docs.size(), equalTo(1));
        assertThat(docs.get(0).body, equalTo("hello world"));
    }
}
