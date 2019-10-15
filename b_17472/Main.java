package b_17472;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

public class Main {
	
	static int map[][] = new int[10][10];
	static boolean visited[][] = new boolean[10][10];
	
	static int dx[] = {0, 1, 0, -1};
	static int dy[] = {-1, 0, 1, 0};
	
	static int N, M;
	
	static int[][] distanceMap = new int[7][7];
	
	static Queue<Point> queue = new LinkedList<>();
	static Queue<Point> tempQueue = new LinkedList<>();
	
	static Map<Integer, Queue<Point>> beachMap = new HashMap<>();
	
	static PriorityQueue<Node> pq = new PriorityQueue<>();
	static boolean[] visitedsum = new boolean[7];
	
	public static void main(String args[]) {
		Scanner scan = new Scanner(System.in);
		
		N = scan.nextInt();
		M = scan.nextInt();
		
		for(int i=0; i<N; i++) {
			for(int j=0; j<M; j++) {
				map[i][j] = scan.nextInt();
			}
		}
		
		int count = 0;
		//group
		for(int i=0; i<N; i++) {
			for(int j=0; j<M; j++) {
				if(map[i][j] != 0 && visited[i][j] == false) {
					count++;
					visited[i][j] = true;
					map[i][j] = count;
					queue.offer(new Point(j, i));
					bfs(count);
				}
			}
		}
		
		for(int i=1; i<7; i++) {
			for(int j=1; j<7; j++) {
				distanceMap[i][j] = 1000;
			}
		}
		
		for(int i=1; i<=count; i++) {
			distance(i);
		}
		
//		for(int i=1; i<=count; i++) {
//			for(int j=1; j<=count; j++) {
//				if(distanceMap[i][j] == 1000) {
//					System.out.print("0 ");
//				}else {
//					System.out.print(distanceMap[i][j] + " ");
//				}
//			}
//			System.out.println("");
//		}
		
		System.out.println(getShortestDistance(count));

		scan.close();
	}
	
	static void bfs(int count) {
		Queue<Point> beachQueue = new LinkedList<>();
		
		Point p;
		int temp_x, temp_y;
		
		while(!queue.isEmpty()) {
			p = queue.poll();
			
			for(int i=0; i<4; i++) {
				temp_x = p.x + dx[i];
				temp_y = p.y + dy[i];
			
				if(checkRange(temp_x, temp_y)) {
					if(map[temp_y][temp_x] != 0) {
						if(visited[temp_y][temp_x] == false) {
							visited[temp_y][temp_x] = true;
							map[temp_y][temp_x] = count;
							tempQueue.add(new Point(temp_x, temp_y));
						}
						
					}else {
//						System.out.println("(" + p.x + ", " + p.y + ", " + i + ")");
						beachQueue.add(new Point(p.x, p.y, i));
					}
				}
			}
			
			if(queue.isEmpty()) {
				queue.addAll(tempQueue);
				tempQueue.clear();
			}
		}
		
//		System.out.println("=====================");
		beachMap.put(count, beachQueue);
	}
	
	static void distance (int idx) {
		
		Point p;
		Queue<Point> bQueue = beachMap.get(idx);
		int count = 0;
		
		int temp_x, temp_y;
		
		next:
		while(!bQueue.isEmpty()) {
			
//			p = bQueue.poll();
//			count = 0;
			
			
			p = bQueue.poll();
	
			temp_x = p.x + dx[p.d];
			temp_y = p.y + dy[p.d];
			count = 0;
			
			//방향으로 계속 더한다.
			while(checkRange(temp_x, temp_y)) {
				if(map[temp_y][temp_x] != 0) {
					if(count >= 2) {
						distanceMap[idx][map[temp_y][temp_x]] = Math.min(distanceMap[idx][map[temp_y][temp_x]], count);
					}
					continue next;
				}else {
					count++;
				}
				temp_x += dx[p.d];
				temp_y += dy[p.d];
			}
		}
			
//			temp_x = p.x;
//			temp_y = p.y;
//			
//			while(checkRange(temp_x+dx[p.d], temp_y+dy[p.d])) {
//				if(map[temp_y][temp_x] > 0 && map[temp_y][temp_x] != idx && count >= 2) {
//					System.out.print("(" + p.x + ", " + p.y + ", " + p.d + ") ::");
//					System.out.println(idx + " : dist : " + distanceMap[idx][map[temp_y][temp_x]]);
//					distanceMap[idx][map[temp_y][temp_x]] =  Math.min(distanceMap[idx][map[temp_y][temp_x]], count);
//					break;
//				}
//				count++;
//				temp_x += dx[p.d];
//				temp_y += dy[p.d];
//			}
//			
//		}
	}
	
	static int getShortestDistance (int count) {
		
		boolean visitAll = true;
		Node node;
		
		int dist = 0;
		visitedsum[1] = true;
		for(int i=1; i<=count; i++) {
			if(distanceMap[1][i] < 1000) {
				pq.offer(new Node(1, i, distanceMap[1][i]));
			}
		}
		
		while(!pq.isEmpty()) {
			visitAll = true;
			node = pq.poll();
			
			if(visitedsum[node.end] == true) {
				continue;
			}
			
			for(int i=1; i<=count; i++) {
				visitAll &= visitedsum[i];
			}
			
			if(visitAll) {
				break;
			}
			
//			System.out.println("(" + node.start + ", " + node.end + ", " + node.cost + ")");
			
			visitedsum[node.end] = true;
			dist += node.cost;
			
			for(int i=1; i<=count; i++) {
				if(distanceMap[node.end][i] < 1000 && visitedsum[i] == false) {
					pq.offer(new Node(node.end, i, distanceMap[node.end][i]));
				}
			}
		}
		
		visitAll = true;
		for(int i=1; i<=count; i++) {
			visitAll &= visitedsum[i];
		}
		
		if(visitAll) {
			return dist;
		}else {
			return -1;
		}
	}
	
	static boolean checkRange (int x, int y) {
		if(x>=0 && x<M && y>=0 && y<N) {
			return true;
		}
		return false;
	}
	
	static void printMap () {
		for(int i=0; i<N; i++) {
			for(int j=0; j<M; j++) {
				System.out.print(map[i][j] + " ");
			}
			System.out.println("");
		}
	}

}

class Point {
	int x, y, d;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Point(int x, int y, int d) {
		this.x = x;
		this.y = y;
		this.d = d;
	}
}

class Node implements Comparable<Node>{
	int start, end, cost;
	
	public Node (int start, int end, int cost) {
		this.start = start;
		this.end = end;
		this.cost = cost;
	}

	@Override
	public int compareTo(Node o) {
		return this.cost > o.cost ? 1 : -1;
	}
}
