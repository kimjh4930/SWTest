package b_17472;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

public class Main {
	
	static int[][] map = new int[10][10];
	static int[][] groupMap = new int[10][10];
	static boolean[][] visited = new boolean[10][10];
	
	static int[] dx = {0, 1, 0, -1};
	static int[] dy = {-1, 0, 1, 0};

	static Queue<Point> queue = new LinkedList<>();
	static Queue<Point> tempQueue = new LinkedList<>();
	static Map<Integer, Queue<Point>> beachMap = new HashMap<>();
	
	static int N, M;
	
	static int[][] bridgeMap = new int[7][7];
	
	static List<int[]> permuList = new ArrayList<>();
	
	public static void main(String args[]) {
		Scanner scan = new Scanner(System.in);
		
		N = scan.nextInt();
		M = scan.nextInt();
		
		for(int i=0; i<N; i++) {
			for(int j=0; j<M; j++) {
				map[i][j] = scan.nextInt();
			}
		}
		
		//group
		int count = 0;
		for(int i=0; i<N; i++) {
			for(int j=0; j<M; j++) {
				if(map[i][j] != 0 && visited[i][j] == false) {
					count++;
					visited[i][j] = true;
					groupMap[i][j] = count;
					queue.add(new Point(j, i));
					bfs(count);
				}
			}
		}
		
		printMap();
		
		for(int i=1; i<7; i++) {
			for(int j=1; j<7; j++) {
				bridgeMap[i][j] = -1;
			}
		}
		
		Iterator<Integer> iter = beachMap.keySet().iterator();
		int idx;
		
		while(iter.hasNext()) {
			idx = iter.next();
			clearVisited();
			tempQueue.clear();
			queue = beachMap.get(idx);
			
			constructBridge(idx);
		}
		
		System.out.println("===========================");
		
		for(int i=1; i<=count; i++) {
			for(int j=1; j<=count; j++) {
				System.out.print(bridgeMap[i][j] + " ");
			}
			System.out.println("");
		}
		
//		int arr[] = new int[count];
//		for(int i=0; i<count; i++) {
//			arr[i] = i+1;
//		}
//		
//		permutation(arr, 0, count, count);
//		
//		int min = (1<<31) - 1;
//		int sum = 0;
//		
//		for(int[] permu : permuList) {
//			sum = 0;
//			for(int i=0; i<permu.length; i++) {
//				
//			}
//		}
		
		scan.close();
	}
	
	static void bfs(int count) {
		Point p;
		
		int temp_x, temp_y;
		
		Queue<Point> beachQueue = new LinkedList<>();
		
		while(!queue.isEmpty()) {
			p = queue.poll();
			
			for(int i=0; i<4; i++) {
				temp_x = p.x + dx[i];
				temp_y = p.y + dy[i];
				
				if(checkRange(temp_x, temp_y) && visited[temp_y][temp_x] == false) {
					if(map[temp_y][temp_x] != 0) {
						groupMap[temp_y][temp_x] = count;
						visited[temp_y][temp_x] = true;
						tempQueue.add(new Point(temp_x, temp_y));
					}else {
						beachQueue.add(new Point(p.x, p.y, i));
					}
				}
			}
			
			if(queue.isEmpty()) {
				queue.addAll(tempQueue);
				tempQueue.clear();
			}
		}
		
		beachMap.put(count, beachQueue);
		
	}
	
	static void constructBridge (int idx) {
		
		int x, y;
		Point p;
		
		int count = 0;
	
		next:
		while(!queue.isEmpty()) {
			p = queue.poll();

			x = p.x + dx[p.d];
			y = p.y + dy[p.d];
			count = 0;
			
			//방향으로 계속 더한다.
			while(checkRange(x, y)) {
				if(groupMap[y][x] != 0) {
					bridgeMap[idx][groupMap[y][x]] = count;
					continue next;
				}else {
					count++;
				}
				x += dx[p.d];
				y += dy[p.d];
			}
		}
		
	}
	
	static void dfs () {
		
	}
	
	static void permutation (int[] arr, int arrSize, int n, int r) {
		if(arrSize == r) {
			permuList.add(arr.clone());
			return;
		}
		
		for(int i=arrSize; i<n; i++) {
			swap(arr, arrSize, i);
			permutation(arr, arrSize+1, n, r);
			swap(arr, arrSize, i);
		}
	}
	
	static void swap(int[] arr, int a, int b) {
		int temp = arr[a];
		arr[a] = arr[b];
		arr[b] = temp;
	}
	
	static boolean checkRange (int x, int y) {
		if(x>=0 && x<M && y>=0 && y<N) {
			return true;
		}
		return false;
	}
	
	static void clearVisited () {
		for(int i=0; i<N; i++) {
			for(int j=0; j<M; j++) {
				visited[i][j] = false;
			}
		}
	}
	
	static void printMap () {
		for(int i=0; i<N; i++) {
			for(int j=0; j<M; j++) {
				System.out.print(groupMap[i][j] + " ");
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
