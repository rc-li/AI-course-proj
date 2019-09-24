package source;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class homework {
	String algo;
	int width;
	int height;
	int landingX;
	int landingY;
	int maxElevation;
	int numTargets;
	int[][] coordinates;
	Node[][] map;

	private class Node implements Comparable<Node>, Cloneable{
		boolean fail;
		int elevation;
		int x;
		int y;
		boolean visited;
		int pathCost;

		ArrayList<Node> pathNodes;

		Node(int elevation) {
			this.elevation = elevation;
			this.pathNodes = new ArrayList<Node>();
		}

		protected Node clone() throws CloneNotSupportedException {
			return (Node) super.clone();
		}

		@Override
		public int compareTo(Node n) {
			// TODO Auto-generated method stub
			return Integer.valueOf(this.pathCost).compareTo(n.pathCost);
		}

	}

	public static void main(String args[]) throws FileNotFoundException, CloneNotSupportedException {
		File file = new File("input_BFS_3.txt");
		homework obj = new homework();
		obj.readInput(file);
		if (obj.algo.equals("BFS")) {
			Node res = obj.UCS();
//			obj.printOutput();
			obj.output(res);
		}
	}

	private void output(Node node) throws FileNotFoundException {
		ArrayList<Node> path = node.pathNodes;
		// System.out.println(path.toString());
		// int[][] sample = {{1,1},{1,1}};
		// System.out.println(Arrays.deepToString(sample));

		// System.out.println(Arrays.deepToString(path));
		// error: deepToString not applicable to a list of Nodes

		String writePath = "output.txt";
		PrintWriter printer = new PrintWriter(writePath);
		if (node.fail) {
			printer.write("FAIL");
		}
		for (Node n : path) {
			printer.write(n.x + "," + n.y + " ");
		}
		printer.close();

	}

	private void readInput(File file) {

		try {
			Scanner scanner = new Scanner(file);
			algo = scanner.next();
			String temp = scanner.next();
			width = Integer.parseInt(temp);
			temp = scanner.next();
			height = Integer.parseInt(temp);
			temp = scanner.next();
			landingX = Integer.parseInt(temp);
			temp = scanner.next();
			landingY = Integer.parseInt(temp);
			temp = scanner.next();
			maxElevation = Integer.parseInt(temp);
			temp = scanner.next();
			numTargets = Integer.parseInt(temp);

//			read the target coordinates
			coordinates = new int[numTargets][2];
			for (int i = 0; i < numTargets; i++) {
				for (int j = 0; j < 2; j++) {
					coordinates[i][j] = Integer.parseInt(scanner.next());
				}
			}

//			read the map
			int elevation;
			map = new Node[height][width];
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					elevation = Integer.parseInt(scanner.next());
					Node node = new Node(elevation);
					node.fail = false;
					node.x = j;
					node.y = i;
					map[i][j] = node;
				}
			}
			scanner.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

//	Function General-Search(problem, Queuing-Fn) returns a solution, or failure
//	nodes  make-queue(make-node(initial-state[problem]))
//	loop do
//		if nodes is empty then return failure
//		node  Remove-Front(nodes)
//		if Goal-Test[problem] applied to State(node) succeeds then return node
//		nodes  Queuing-Fn(nodes, Expand(node, Operators[problem]))
//	end

//	**actually this part is not necessary
//	private boolean allVisited(Node[][] map) {
//		for (int i = 0; i < height; i++) {
//			for (int j = 0; j < width; j++) {
//				if (!map[i][j].visited)
//					return false;
//			}
//		}
//		return true;
//	}

	private Node BFS() {
		LinkedList<Node> queue = new LinkedList<Node>();
//		map[y][x].pathNodes.add(map[y][x]);
		map[landingY][landingX].visited = true;
		queue.add(map[landingY][landingX]);
//		while (!allVisited(map)) {
		while (true) {
			if (queue.isEmpty()) {
				Node node = new Node(0);
				node.fail = true;
				return node;
			}
			Node node = queue.removeFirst();
			if (node.x == coordinates[0][0] && node.y == coordinates[0][1]) {
				node.pathNodes.add(node);
				return node;
			}
			expandNeighborhood_BFS(node, queue);
		}
	}

	private void expandOneNode_BFS(int x, int y, Node node, Queue<Node> queue, boolean isDiagonal) {
		if (x >= 0 && x < width && y >= 0 && y < height) {
			Node neighbor = map[y][x];
			if (!neighbor.visited && Math.abs(neighbor.elevation - node.elevation) <= maxElevation) {
				neighbor.pathNodes.addAll(node.pathNodes);
				neighbor.pathNodes.add(node);
				neighbor.visited = true;
				if (isDiagonal) {
					neighbor.pathCost = node.pathCost + 14;
				} else {
					neighbor.pathCost = node.pathCost + 10;
				}
				queue.add(neighbor);
			}
		}
	}

	// !!!>>>> actually the x, y here are never used, too lazy to get rid of them
	// private void queuing(int x, int y, Node node, Queue<Node> queue) {
	// actually i should get rid of them!! too verbose
	private void expandNeighborhood_BFS(Node node, Queue<Node> queue) {
		expandOneNode_BFS(node.x - 1, node.y - 1, node, queue, true);
		expandOneNode_BFS(node.x - 1, node.y, node, queue, false);
		expandOneNode_BFS(node.x - 1, node.y + 1, node, queue, true);
		expandOneNode_BFS(node.x, node.y - 1, node, queue, false);
		expandOneNode_BFS(node.x, node.y + 1, node, queue, false);
		expandOneNode_BFS(node.x + 1, node.y - 1, node, queue, true);
		expandOneNode_BFS(node.x + 1, node.y, node, queue, false);
		expandOneNode_BFS(node.x + 1, node.y + 1, node, queue, true);

	}

	private Node UCS() {
		LinkedList<Node> openNodes = new LinkedList<homework.Node>();
		openNodes.add(map[landingY][landingX]);
		LinkedList<Node> closedNodes = new LinkedList<homework.Node>();
		LinkedList<Node> childreNodes = new LinkedList<homework.Node>();
		Node child;
		while (true) {
			if (openNodes.isEmpty()) {
				Node node = new Node(0);
				node.fail = true;
				return node;
			}
			Node curNode = openNodes.removeFirst();
			if (curNode.x == coordinates[0][0] && curNode.y == coordinates[0][1]) {
				curNode.pathNodes.add(curNode);
				return curNode;
			}
			expandNeighborhood_UCS(curNode, childreNodes);
			while (!childreNodes.isEmpty()) {
				child = childreNodes.removeFirst();
				if (!openNodes.contains(child) && !closedNodes.contains(child)) {
					if (Math.abs(child.x - curNode.x) + Math.abs(child.y - curNode.y) == 2) {
						openNodes.add(child);
						child.pathCost = curNode.pathCost + 14;
						child.pathNodes.addAll(curNode.pathNodes);
						child.pathNodes.add(child);
					} else {
						openNodes.add(child);
						child.pathCost = curNode.pathCost + 10;
						child.pathNodes.addAll(curNode.pathNodes);
						child.pathNodes.add(child);
					}
				} else if (openNodes.contains(child)) {
					if (Math.abs(child.x - curNode.x) + Math.abs(child.y - curNode.y) == 2) {
						if (curNode.pathCost + 14 < child.pathCost) {
							child.pathCost = curNode.pathCost + 14;
							child.pathNodes = curNode.pathNodes;
							child.pathNodes.add(child);
						}
					}
					else {
						if (curNode.pathCost + 10 < child.pathCost) {
							child.pathCost = curNode.pathCost + 10;
							child.pathNodes = curNode.pathNodes;
							child.pathNodes.add(child);
						}
					}
				} else if (closedNodes.contains(child)) {
					if (Math.abs(child.x - curNode.x) + Math.abs(child.y - curNode.y) == 2) {
						if (curNode.pathCost + 14 < child.pathCost) {
							child.pathCost = curNode.pathCost + 14;
							child.pathNodes = curNode.pathNodes;
							child.pathNodes.add(child);
							openNodes.add(child);
						}
					}
					else {
						if (curNode.pathCost + 10 < child.pathCost) {
							child.pathCost = curNode.pathCost + 10;
							child.pathNodes = curNode.pathNodes;
							child.pathNodes.add(child);
							openNodes.add(child);
						}
					}
				}
			}
			closedNodes.add(curNode);
			Collections.sort(openNodes);
		}
	}

	private void expandOneNode_UCS(int x, int y, Node node, Queue<Node> queue, boolean isDiagonal) {
		if (x >= 0 && x < width && y >= 0 && y < height) {
			Node neighbor = map[y][x];
			if (Math.abs(neighbor.elevation - node.elevation) <= maxElevation) {
//				neighbor.pathNodes = node.pathNodes;
//				neighbor.pathNodes.addAll(node.pathNodes);
//				neighbor.pathNodes.add(node);
				queue.add(neighbor);
				
				
			}
		}
	}

	private void expandNeighborhood_UCS(Node node, Queue<Node> queue) {
		expandOneNode_UCS(node.x - 1, node.y - 1, node, queue, true);
		expandOneNode_UCS(node.x - 1, node.y, node, queue, false);
		expandOneNode_UCS(node.x - 1, node.y + 1, node, queue, true);
		expandOneNode_UCS(node.x, node.y - 1, node, queue, false);
		expandOneNode_UCS(node.x, node.y + 1, node, queue, false);
		expandOneNode_UCS(node.x + 1, node.y - 1, node, queue, true);
		expandOneNode_UCS(node.x + 1, node.y, node, queue, false);
		expandOneNode_UCS(node.x + 1, node.y + 1, node, queue, true);

	}

//	private void printOutput() {
//		File file = new File("output.txt");
//		
//		try {
//			if (file.createNewFile())
//				System.out.println("output file successfully created!");
//			else
//				System.out.println("was not created :(");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}

}
