package restcora;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.index.Index;
import org.neo4j.rest.graphdb.RestGraphDatabase;
import common.RelTypes;

public class PrintPaperAuthors {
    public static void main(String[] args) {
        // The paper titles to search for.
        String searchString = "Generating*";

        // Open the database read only
        String DB_PATH = "http://puccini.cs.lth.se:7474/db/data";
        GraphDatabaseService db = new RestGraphDatabase(DB_PATH);
        registerShutdownHook(db);

        Index<Node> paperIndex = db.index().forNodes("paperIndex");
        for (Node p : paperIndex.query("title", searchString)) {
            System.out.println(p.getProperty("title") + ", "
                    + p.getProperty("year"));
            System.out.print("    Authors: ");
            for (Relationship r : p.getRelationships(RelTypes.AUTHORED_BY)) {
                Node author = r.getEndNode();
                System.out.print(author.getProperty("lastName") + ","
                        + (char)((Integer) author.getProperty("initial")).intValue() + " / ");
            }
            System.out.println();
            System.out.println();
        }

        db.shutdown();
    }

    private static void registerShutdownHook(final GraphDatabaseService db) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                db.shutdown();
            }
        });
    }
}
