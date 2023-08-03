package 행렬계산;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Calculator {
	private Matrix[] matrix; //행렬 배열
	
	public Calculator() {
		//empty
	}
	
	public void run() throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); //입력을 받기 위함
		StringTokenizer st; //입력을 받기위함
		
		final int MULTIPLY = 1, DETERMINANT = 2, INVERSE = 3, EQUATION = 4, EXIT = 5; //동작들
		
		while(true) {
			System.out.print("원하시는 항목을 선택해 주세요(1 행렬곱 연산, 2 행렬식 연산, 3 역행렬, 4 방정식 ,5 종료): ");
			st = new StringTokenizer(br.readLine());
			int command  = Integer.parseInt(st.nextToken()); //명령을 읽어들임

			switch(command) {
			case MULTIPLY:
				getMatrix(2);
				System.out.println("행렬의 곱 결과\n" + Matrix.multiply(matrix[0],matrix[1]));
				break;
				
			case DETERMINANT:
				getMatrix(1);
				System.out.println("행렬식: "+matrix[0].getDeterminant());
				break;
				
			case INVERSE:
				getMatrix(1);
				System.out.println("역행렬:\n"+matrix[0].getInverse());
				break;	

			case EQUATION:
				System.out.println("AX = B에서 A, B순으로 입력 해 주세요");
				getMatrix(2);
				System.out.println("해\n" + matrix[0].getSolution(matrix[1]));
				break;
				
			case EXIT:
				System.out.println("프로그램을 종료합니다. 이용해 주셔서 감사합니다.");
				return;
				
			default:
				System.out.println("잘못된 입력!")	;
			}
		}
		
	}
	
	public void getMatrix(int numMatrix) throws IOException{//numMatrix만큼 행렬을 읽어들임
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		RationalNum[][] matrixBuff; //행렬 buffer;
		
		matrix = new Matrix[numMatrix];
	
		for(int i=0; i<numMatrix; i++) {
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
	}
}
