package myapp.search;

import com.github.tlrx.elasticsearch.test.EsSetup;
import myapp.model.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.github.tlrx.elasticsearch.test.EsSetup.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Author: chris
 * Created: 8/25/13
 */
public class DSLExample {

    EsSetup esSetup;
    SearchEngine searchEngine;

    @Before
    public void setupES() {
        esSetup = new EsSetup();
        esSetup.execute(
                createIndex(SearchEngine.Constants.INDEX_MYAPP)
                .withMapping(SearchEngine.Constants.MAPPING_TYPE_DOCS, fromClassPath("mapping.json"))
                .withData(fromClassPath("data.json"))
        );
        searchEngine = new SearchEngine(esSetup.client());
    }

    @After
    public void tearDownES() {
        esSetup.terminate();
    }

    @Test
    public void returnsDocumentsThatMatchQuery() {
        List<Document> docs = searchEngine.search("hello");

        assertThat(docs.size(), equalTo(1));
        assertThat(docs.get(0).body, equalTo("hello world"));
    }

}

