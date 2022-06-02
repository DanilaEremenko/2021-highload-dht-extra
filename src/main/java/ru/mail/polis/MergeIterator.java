package ru.mail.polis;

import java.util.Iterator;
import java.util.Map;

public class MergeIterator implements Iterator<Record> {
    Map<Node, PeekingIterator<Record>> peekingIteratorMap;

    MergeIterator(Map<Node, PeekingIterator<Record>> peekingIteratorMap) {
        this.peekingIteratorMap = peekingIteratorMap;
    }

    @Override
    public boolean hasNext() {
        for (Map.Entry<Node, PeekingIterator<Record>> entry : peekingIteratorMap.entrySet())
            if (entry.getValue().hasNext())
                return true;

        return false;

    }

    @Override
    public Record next() {
        NodeData bestData = null;

        for (Map.Entry<Node, PeekingIterator<Record>> entry : this.peekingIteratorMap.entrySet()) {
            if (!entry.getValue().hasNext())
                continue;

            NodeData currData = new NodeData(entry.getKey(), entry.getValue(), entry.getValue().peek());

            if (bestData == null) {
                bestData = currData;
                continue;
            }

            int compRes = bestData.record.compareTo(currData.record);
            if (compRes < 0)
                bestData = currData;

        }


        assert bestData != null;

        for (Map.Entry<Node, PeekingIterator<Record>> entry : this.peekingIteratorMap.entrySet()) {
            NodeData currData = new NodeData(entry.getKey(), entry.getValue(), entry.getValue().peek());

            if (!currData.iterator.hasNext())
                continue;


            int keyComp = bestData.record.key.compareTo(currData.record.key);
            if (keyComp < 0) {
                currData.node.update(bestData.record);
                continue;
            }

            int compRes = bestData.record.compareTo(currData.record);
            if (compRes > 0) {
                currData.node.update(bestData.record);
                currData.iterator.next();
            } else {
                currData.iterator.next();
            }
        }

        return bestData.record;


    }

    static class NodeData {
        private final Node node;
        private final Iterator<Record> iterator;
        private final Record record;

        NodeData(Node node, Iterator<Record> iterator, Record record) {
            this.node = node;
            this.iterator = iterator;
            this.record = record;
        }

    }
}
