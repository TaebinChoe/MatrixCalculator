package 행렬계산;

/*
 * 행렬을 나타내며
 * 다양한 행렬 계산 기능을 제공한다.
 */

public class Matrix{//행렬
	RationalNum[][] matrix; //2차원 유리수 배열
	int row; //행 크기
	int col; //열 크기

	public Matrix(RationalNum[][] matrix){
		this.matrix = matrix;
		row = matrix.length;
		col = matrix[0].length;
	}
	
	/*
	 * 넘겨받은 두행렬을 곱해서 결과를 반환한다.
	 */
	public static Matrix multiply(Matrix m1, Matrix m2){ //왼쪽에 곱한 결과를 반환
		
		if(m1.col != m2.row) {
			System.out.println("곱의 형태가 맞지 않습니다.");
			return null;
		}

		RationalNum[][] result = new RationalNum[m1.row][m2.col];//결과를 저장

		for(int r=0; r<m1.row; r++) {
			for(int c=0; c<m2.col; c++) {
				//this의 r행벡터와 cMatrix의 c열 벡터를 내적하여 결과 행렬의 r행 c열에 저장 한다.
				result[r][c] = new RationalNum();
				for(int i=0; i<m1.col; i++) {
					result[r][c].add(RationalNum.multiply(m1.matrix[r][i],m2.matrix[i][c]));
				}
			}
		}

		return new Matrix(result);
	}
	
	/*
	 * 행렬식을 구하는 함수
	 */
	public RationalNum getDeterminant() {
		if(row != col) {
			System.out.println("정방 행렬만 행렬식을 구할 수 있습니다.");
		}
		boolean[][] isHidden = new boolean[2][col]; //가려진 행 열을 표시 0행-행 1행-열
		
		return determinant(row, isHidden); //0행0열 부터 size가 row인 소행렬 식을 구해서 반환한다.
	}
	
	//소행렬식을 구하는 함수, 재귀적으로 정의
	private RationalNum determinant(int size, boolean[][] isHidden) {//size*size 크기의 소행렬식을 구함
		int startRow = 0;//시작 행
		int startCol = 0;//시작 열
		
		while(isHidden[0][startRow]) {//가려지지 않은 최 상단 행을 찾음
			startRow++;
		}
		while(isHidden[1][startCol]) {//가려지지 않은 가장 좌측 열을 찾음
			startCol++;
		}
		
		if(size == 1) {//종결 조건
			return matrix[startRow][startCol];
		}
		isHidden[0][startRow] = true; //현재행을 가림
		
		RationalNum result = new RationalNum();//결과를 저장
		
		int curCol = startCol;
		for(int i=0; i<size; i++) {//size번의 소행렬식을 구해 더해야함
			
			isHidden[1][curCol] = true; //현재 열 가림
		
			if(i%2 == 0){//홀수번째면 더함
				result.add(RationalNum.multiply(matrix[startRow][curCol], determinant(size-1,isHidden)));
				//System.out.println(result);
			}else {//짝수번째면 뺌
				result.subtract(RationalNum.multiply(matrix[startRow][curCol], determinant(size-1,isHidden)));
				//System.out.println(result);
			}
			
			isHidden[1][curCol] = false; //가림 해제
			
			curCol = nextCol(curCol,isHidden); //다음 가리지 않은 열을 찾음
		}
		
		isHidden[0][startRow] = false; //현재열 가림 해제
		return result;
	}
	
	//가려지지 않은 다음 왼쪽 열을 찾아 주는 함수
	private int nextCol(int curCol,boolean[][] isHidden) {
		int nextCol = curCol+1;
		
		while(nextCol < col) {
			if(!isHidden[1][nextCol]) {//가려 지지 않았다면
				return nextCol;
			}
			nextCol++;
		}
		
		return -1; //다음 열 없음
	}
	
	/*
	 * 역행렬을 구해서 반환 하는 함수
	 */
	public Matrix getInverse() {
		RationalNum determinant = getDeterminant(); //행렬식
		
		if(row != col) {//정방행렬이 아닌경우
			System.out.println("정방 행렬만 행렬식을 구할 수 있습니다.");
			return null;
		}
		
		if(determinant.equals(new RationalNum(1,0))) {//비가역 행렬인 경우
			System.out.println("비가역 행렬입니다.");
			return null;
		}
		
		RationalNum[][] adjointArr = new RationalNum[row][col];
		
		boolean[][] isHidden = new boolean[2][col]; //가려진 행 열을 표시 0행-행 1행-열
		
		for(int r=0; r<row; r++) {
			
			for(int c=0; c<col; c++) {
				//r행 c열 가림
				isHidden[0][r] = true;
				isHidden[1][c] = true;
				//Crc를 계산해서 determinant로 나눈 후 c행 r열에 저장
				
				adjointArr[c][r] = RationalNum.divide(determinant(col-1,isHidden),determinant); 
				if((r+c) %2 == 1) adjointArr[c][r].multiply(new RationalNum(1,-1));
				
				//가림 해제
				isHidden[0][r] = false;
				isHidden[1][c] = false;
				
			}
		}
		
		
		return new Matrix(adjointArr);
	}
	
	
	public String toString() {
		StringBuffer rStr = new StringBuffer();
		
		for(int r=0; r<row ; r++) {
			for(int c=0; c<col; c++) {
				rStr.append(matrix[r][c] + "\t");
			}
			rStr.append("\n");
		}
		
		return rStr.toString();
	}
	
}
