package map;

import java.util.HashMap;
import java.util.Stack;

public interface Search {
    Stack<Node> findPath(Node start, Node end);
    Stack<Node> returnPath(Node currentNode, HashMap<Node, Node> cameFrom);
    double getEuclideanDistance(Node start, Node end);
}
