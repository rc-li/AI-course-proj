package source;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
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

	private class Node implements Comparable<Node>, Cloneable {
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

	public static void main(String args[]) throws CloneNotSupportedException, IOException {
		File file = new File("input.txt");
		homework obj = new homework();
		obj.readInput(file);
		String writePath = "output.txt";
		PrintWriter printer = new PrintWriter(writePath);
		if (obj.algo.equals("BFS")) {
			for (int n = 0; n < obj.numTargets; n++) {
				boolean isLastLine = false;
				if (n == obj.numTargets - 1)
					isLastLine = true;
				Node res = obj.BFS(n);
				obj.output(res, printer, isLastLine);
				obj.resetMap();
			}
		}
		if (obj.algo.equals("UCS")) {
//			System.out.println("UCS started at " + new Timestamp(System.currentTimeMillis()));
			for (int n = 0; n < obj.numTargets; n++) {
				boolean isLastLine = false;
				if (n == obj.numTargets - 1)
					isLastLine = true;
				Node res = obj.UCS(n);
				obj.output(res, printer, isLastLine);
				obj.resetMap();
			}
		}
		if (obj.algo.equals("A*")) {
//			System.out.println("A* started at " + new Timestamp(System.currentTimeMillis()));
			for (int n = 0; n < obj.numTargets; n++) {
				boolean isLastLine = false;
				if (n == obj.numTargets - 1)
					isLastLine = true;
				Node res = obj.A_Star(n);
				obj.output(res, printer, isLastLine);
				obj.resetMap();
			}
		}
		printer.close();
	}

	private void resetMap() {
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				map[i][j].visited = false;
				map[i][j].pathNodes.clear();
			}
		}
	}

	private void output(Node node, PrintWriter printer, boolean isLastLine) throws IOException {
		ArrayList<Node> path = node.pathNodes;
		if (node.fail) {
			printer.write("FAIL");
		}
//		System.out.println("at " + new Timestamp(System.currentTimeMillis()) + " found path ");
		StringWriter stringwriter = new StringWriter();
		for (Node n : path) {
			stringwriter.append(n.x + "," + n.y + " ");
		}
		String temp = stringwriter.toString();
		temp = temp.trim();
		printer.write(temp);
//		System.out.println(temp);
		if (!isLastLine)
			printer.write("\n");
		stringwriter.close();
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

	private Node BFS(int landSiteNO) {
		LinkedList<Node> queue = new LinkedList<Node>();
		map[landingY][landingX].visited = true;
		queue.add(map[landingY][landingX]);
		while (true) {
			if (queue.isEmpty()) {
				Node node = new Node(0);
				node.fail = true;
				return node;
			}
			Node node = queue.removeFirst();
			if (node.x == coordinates[landSiteNO][0] && node.y == coordinates[landSiteNO][1]) {
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

	private Node UCS(int landSiteNO) {
		PriorityQueue<Node> openNodes = new PriorityQueue<homework.Node>();
		Node landingPoint = map[landingY][landingX];
		landingPoint.pathNodes.add(landingPoint);
		openNodes.offer(landingPoint);
		LinkedHashSet<Node> closedNodes = new LinkedHashSet<homework.Node>();
		LinkedList<Node> childreNodes = new LinkedList<homework.Node>();
		Node child;
		while (true) {
			if (openNodes.isEmpty()) {
				Node node = new Node(0);
				node.fail = true;
				return node;
			}
			Node curNode = openNodes.poll();
			if (curNode.x == coordinates[landSiteNO][0] && curNode.y == coordinates[landSiteNO][1]) {
				return curNode;
			}
			expandNeighborhood_UCS(curNode, childreNodes);
			while (!childreNodes.isEmpty()) {
				child = childreNodes.removeFirst();
				if (!openNodes.contains(child) && !closedNodes.contains(child)) {
					if (Math.abs(child.x - curNode.x) + Math.abs(child.y - curNode.y) == 2) {
						child.pathCost = curNode.pathCost + 14;
						child.pathNodes.clear();
						child.pathNodes.addAll(curNode.pathNodes);
						child.pathNodes.add(child);
						openNodes.offer(child);
					} else {
						child.pathCost = curNode.pathCost + 10;
						child.pathNodes.clear();
						child.pathNodes.addAll(curNode.pathNodes);
						child.pathNodes.add(child);
						openNodes.offer(child);
					}
				} else if (openNodes.contains(child)) {
					if (Math.abs(child.x - curNode.x) + Math.abs(child.y - curNode.y) == 2) {
						if (curNode.pathCost + 14 < child.pathCost) {
							child.pathCost = curNode.pathCost + 14;
							child.pathNodes.clear();
							child.pathNodes.addAll(curNode.pathNodes);
							child.pathNodes.add(child);
							openNodes.offer(child);
						}
					} else {
						if (curNode.pathCost + 10 < child.pathCost) {
							child.pathCost = curNode.pathCost + 10;
							child.pathNodes.clear();
							child.pathNodes.addAll(curNode.pathNodes);
							child.pathNodes.add(child);
							openNodes.offer(child);
						}
					}
				} else if (closedNodes.contains(child)) {
					if (Math.abs(child.x - curNode.x) + Math.abs(child.y - curNode.y) == 2) {
						if (curNode.pathCost + 14 < child.pathCost) {
							child.pathCost = curNode.pathCost + 14;
							child.pathNodes.clear();
							child.pathNodes.addAll(curNode.pathNodes);
							child.pathNodes.add(child);
							closedNodes.remove(child);
							openNodes.offer(child);
						}
					} else {
						if (curNode.pathCost + 10 < child.pathCost) {
							child.pathCost = curNode.pathCost + 10;
							child.pathNodes.clear();
							child.pathNodes.addAll(curNode.pathNodes);
							child.pathNodes.add(child);
							closedNodes.remove(child);
							openNodes.offer(child);
						}
					}
				}
			}
			closedNodes.add(curNode);
		}
	}

	private void expandOneNode_UCS(int x, int y, Node node, Queue<Node> queue, boolean isDiagonal) {
		if (x >= 0 && x < width && y >= 0 && y < height) {
			Node neighbor = map[y][x];
			if (Math.abs(neighbor.elevation - node.elevation) <= maxElevation) {
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

	private Node A_Star(int landSiteNO) {
		PriorityQueue<Node> openNodes = new PriorityQueue<homework.Node>();
		Node landingPoint = map[landingY][landingX];
		landingPoint.pathNodes.add(landingPoint);
		openNodes.offer(landingPoint);
		LinkedHashSet<Node> closedNodes = new LinkedHashSet<homework.Node>();
		LinkedList<Node> childreNodes = new LinkedList<homework.Node>();
		Node child;
		while (true) {
			if (openNodes.isEmpty()) {
				Node node = new Node(0);
				node.fail = true;
				return node;
			}
			Node curNode = openNodes.poll();
			if (curNode.x == coordinates[landSiteNO][0] && curNode.y == coordinates[landSiteNO][1]) {
				return curNode;
			}
			expandNeighborhood_UCS(curNode, childreNodes);
			while (!childreNodes.isEmpty()) {
				child = childreNodes.removeFirst();
				if (!openNodes.contains(child) && !closedNodes.contains(child)) {
					if (Math.abs(child.x - curNode.x) + Math.abs(child.y - curNode.y) == 2) {
						child.pathCost = curNode.pathCost + 14 + Math.abs(curNode.elevation - child.elevation);
						child.pathNodes.clear();
						child.pathNodes.addAll(curNode.pathNodes);
						child.pathNodes.add(child);
						openNodes.offer(child);
					} else {
						child.pathCost = curNode.pathCost + 10 + Math.abs(curNode.elevation - child.elevation);
						child.pathNodes.clear();
						child.pathNodes.addAll(curNode.pathNodes);
						child.pathNodes.add(child);
						openNodes.offer(child);
					}
				} else if (openNodes.contains(child)) {
					if (Math.abs(child.x - curNode.x) + Math.abs(child.y - curNode.y) == 2) {
						if (curNode.pathCost + 14 + Math.abs(curNode.elevation - child.elevation) < child.pathCost) {
							child.pathCost = curNode.pathCost + 14 + Math.abs(curNode.elevation - child.elevation);
							child.pathNodes.clear();
							child.pathNodes.addAll(curNode.pathNodes);
							child.pathNodes.add(child);
							openNodes.offer(child);
						}
					} else {
						if (curNode.pathCost + 10 + Math.abs(curNode.elevation - child.elevation) < child.pathCost) {
							child.pathCost = curNode.pathCost + 10 + Math.abs(curNode.elevation - child.elevation);
							child.pathNodes.clear();
							child.pathNodes.addAll(curNode.pathNodes);
							child.pathNodes.add(child);
							openNodes.offer(child);
						}
					}
				} else if (closedNodes.contains(child)) {
					if (Math.abs(child.x - curNode.x) + Math.abs(child.y - curNode.y) == 2) {
						if (curNode.pathCost + 14 + Math.abs(curNode.elevation - child.elevation) < child.pathCost) {
							child.pathCost = curNode.pathCost + 14 + Math.abs(curNode.elevation - child.elevation);
							child.pathNodes.clear();
							child.pathNodes.addAll(curNode.pathNodes);
							child.pathNodes.add(child);
							closedNodes.remove(child);
							openNodes.offer(child);
						}
					} else {
						if (curNode.pathCost + 10 + Math.abs(curNode.elevation - child.elevation) < child.pathCost) {
							child.pathCost = curNode.pathCost + 10 + Math.abs(curNode.elevation - child.elevation);
							child.pathNodes.clear();
							child.pathNodes.addAll(curNode.pathNodes);
							child.pathNodes.add(child);
							closedNodes.remove(child);
							openNodes.offer(child);
						}
					}
				}
			}
			closedNodes.add(curNode);
		}
	}


}
