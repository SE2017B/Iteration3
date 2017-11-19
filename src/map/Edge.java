//we need to push this tonight

package map;

public class Edge {
    private Node nodeOne;
    private Node nodeTwo;
    private String ID;
    private double cost;

    public Edge(String ID, Node nodeOne, Node nodeTwo){
        this.ID = ID;
        this.nodeOne = nodeOne;
        this.nodeTwo = nodeTwo;
        this.cost = this.nodeOne.getEuclidianDistance(this.nodeTwo);
        this.nodeOne.addConnection(this);
        this.nodeTwo.addConnection(this);
    }

    // need a function for getting ALL sibling nodes

    //Getters
    public String getID(){
        return ID;
    }
    public double getCost() {
        return cost;
    }
    public Node getNodeOne(){
        return this.nodeOne;
    }
    public Node getNodeTwo(){
        return this.nodeTwo;
    }
    public Node getOtherNode(Node node){
        if(node.equals(nodeOne)) return nodeTwo;
        else return nodeOne;
    }
}
