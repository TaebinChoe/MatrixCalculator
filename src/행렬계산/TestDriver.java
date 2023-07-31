package 행렬계산;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class TestDriver {
	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		Matrix matrix;//행렬
		
		System.out.println("원하는 행렬의 크기를 입력하시오(행, 열)");
		
		StringTokenizer st = new StringTokenizer(br.readLine());//읽어들임
		RationalNum[][] matrixBuff = new RationalNum[Integer.parseInt(st.nextToken())][Integer.parseInt(st.nextToken())];
		//입력을 받음
		System.out.println("행렬을 입력하시오");
		for(int r=0; r<matrixBuff.length; r++) {
			st = new StringTokenizer(br.readLine());
			for(int c=0; c<matrixBuff[0].length; c++) {
				matrixBuff[r][c] = new RationalNum(1,Integer.parseInt(st.nextToken()));
			}
		}
		matrix = new Matrix(matrixBuff); //행렬 객체를 만들어서 저장
		System.out.println("입력하신 행렬:");
		matrix.print();

		System.out.println("행렬식: "+matrix.getDeterminant());

	}
}
