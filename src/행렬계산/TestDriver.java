package 행렬계산;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class TestDriver {
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		final int MULTIPLY = 1, DETERMINANT = 2, EXIT = 3;
		StringTokenizer st;
		int command; //명령
		RationalNum[][] matrixBuff; //행렬 buffer;
		Matrix[] matrix; //행렬 배열
		
		while(true) {
			System.out.print("원하시는 항목을 선택해 주세요(1 행렬곱 연산, 2 행렬식 연산, 3 종료): ");
			st = new StringTokenizer(br.readLine());
			command  = Integer.parseInt(st.nextToken()); //명령을 읽어들임

			switch(command) {
			case MULTIPLY:
				matrix = new Matrix[2];
				
				for(int i=0; i<2; i++) {
					System.out.println("원하는 행렬의 크기를 입력하시오(행, 열)");
					st = new StringTokenizer(br.readLine());//읽어들임
					matrixBuff = new RationalNum[Integer.parseInt(st.nextToken())][Integer.parseInt(st.nextToken())];
					//입력을 받음
					System.out.println("행렬을 입력하시오.");
					if(i==0) {
						System.out.println("(예시)\n1 2\n3 4");
					}
					for(int r=0; r<matrixBuff.length; r++) {
						st = new StringTokenizer(br.readLine());
						for(int c=0; c<matrixBuff[0].length; c++) {
							matrixBuff[r][c] = new RationalNum(1,Integer.parseInt(st.nextToken()));
						}
					}
					matrix[i]= new Matrix(matrixBuff); //행렬 객체를 만들어서 저장
					System.out.println("입력하신 행렬:");
					System.out.println(matrix[i]);
				}
				
				System.out.println("행렬의 곱 결과\n" + Matrix.multiply(matrix[0],matrix[1]));
				
				break;
				
			case DETERMINANT:
				matrix = new Matrix[1];
				System.out.println("원하는 행렬의 크기를 입력하시오.(행, 열)");
				st = new StringTokenizer(br.readLine());//읽어들임
				matrixBuff = new RationalNum[Integer.parseInt(st.nextToken())][Integer.parseInt(st.nextToken())];
				//입력을 받음
				System.out.println("행렬을 입력하시오. \n(예시)\n1 2\n3 4");
				for(int r=0; r<matrixBuff.length; r++) {
					st = new StringTokenizer(br.readLine());
					for(int c=0; c<matrixBuff[0].length; c++) {
						matrixBuff[r][c] = new RationalNum(1,Integer.parseInt(st.nextToken()));
					}
				}
				matrix[0] = new Matrix(matrixBuff); //행렬 객체를 만들어서 저장
				System.out.println("입력하신 행렬:");
				System.out.println(matrix[0]);

				System.out.println("행렬식: "+matrix[0].getDeterminant());
				break;
				
			case EXIT:
				return;
				
			default:
				System.out.println("잘못된 입력!")	;
			}

		}
	}
}
