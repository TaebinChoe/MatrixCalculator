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
		
		final int MULTIPLY = 1, DETERMINANT = 2, INVERSE = 3, EQUATION_I = 4,EQUASION_G = 5, EXIT = 6; //동작들
		
		while(true) {
			System.out.print("\n==========<MENU>==========\n1. 행렬곱 연산\n2. 행렬식 연산\n3. 역행렬\n4. 방정식(역행렬)\n5. 방정식(가우스-조르단 소거법)\n6. 종료\n입력: ");
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

			case EQUATION_I:
				System.out.println("AX = B에서 A, B순으로 입력 해 주세요");
				getMatrix(2);
				System.out.println("<해>\n" + matrix[0].getSolutionByInverse(matrix[1]));
				break;
				
			case EQUASION_G:
				System.out.println("AX = B에서 A의 열의 수(미지수 수)를 입력해주세요.");
				System.out.print("입력: ");
				
				st = new StringTokenizer(br.readLine());
				int[] resultBox = new int[1];
				String[] solutionBox = new String[1];
				
				System.out.println("A|B를 입력해주세요.");
				
				getMatrix(1);
				Matrix solution = Matrix.getSolutionByGauss(matrix[0],Integer.parseInt(st.nextToken()),resultBox, solutionBox);
				
				switch(resultBox[0]) {
				case Matrix.UNIQUE_SOLUTION:
					System.out.println("<해>\n" + solution);
					break;
				case Matrix.COUNTLESS_SOLUTION:
					System.out.println("무수히 많은 해를 가지므로 해를 벡터 방정식으로 표현해 드리겠습니다.");
					System.out.println("벡터 방정식: " + solutionBox[0]);
					System.out.println();
					
					break;
				case Matrix.NO_SOLUTION:
					System.out.println("해를 가지지 않습니다.");
					break;
				}	
				break;
			case EXIT:
				System.out.println("프로그램을 종료합니다. 이용해 주셔서 감사합니다.");
				return;
				
			default:
				System.out.println("잘못된 입력!")	;
			}
			System.out.println("==========================");
		}
		
	}
	
	public void getMatrix(int numMatrix) throws IOException{//numMatrix만큼 행렬을 읽어들임
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		RationalNum[][] matrixBuff; //행렬 buffer;
		
		matrix = new Matrix[numMatrix];
	
		for(int i=0; i<numMatrix; i++) {
			System.out.println("원하는 행렬의 크기를 입력하시오(행, 열)");
			System.out.print("입력: ");
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
			System.out.println("<입력하신 행렬>");
			System.out.println(matrix[i]);
		}
	}
}
