package com.devtools.worker.tools.collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;

public class CollectionTools {
    private static Logger logger = LoggerFactory.getLogger(CollectionTools.class);

    public static void maplog(Map map) {
        StringBuffer sb = new StringBuffer();

        Iterator<String> kit = map.keySet().iterator();

        sb.append("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
        while (kit.hasNext()) {
            String k = kit.next();
            sb.append("{").append(k).append(":").append(map.get(k).toString()).append("}\n");
        }
        sb.append("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");

        logger.info(sb.toString());
    }
}
