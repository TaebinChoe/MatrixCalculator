package 행렬계산;

/*
 * 행렬을 나타내며
 * 다양한 행렬 계산 기능을 제공한다.
 */

public class Matrix{//행렬
	RationalNum[][] matrix; //2차원 유리수 배열
	int row; //행 크기
	int col; //열 크기

	Matrix(RationalNum[][] matrix){
		this.matrix = matrix;
		row = matrix.length;
		col = matrix[0].length;
	}

	Matrix multiplyToLeft(Matrix counterPart){ //왼쪽에 곱한 결과를 반환
		RationalNum[][] cMatrix = counterPart.matrix;

		int cRow = cMatrix.length; //상대 행렬 행 크기
		int cCol = cMatrix[0].length; //상대 행렬 열 크기

		if(col != cRow) {
			System.out.println("곱의 형태가 맞지 않음");
			return null;
		}

		RationalNum[][] result = new RationalNum[row][cCol];//결과를 저장

		for(int r=0; r<row; r++) {
			for(int c=0; c<cCol; c++) {
				//this의 r행벡터와 cMatrix의 c열 벡터를 내적하여 결과 행렬의 r행 c열에 저장 한다.
				result[r][c] = new RationalNum();
				for(int i=0; i<col; i++) {
					result[r][c].add(RationalNum.multiply(matrix[r][i],cMatrix[i][c]));
				}
			}
		}

		return new Matrix(result);
	}

	RationalNum getDeterminant() {
		if(row != col) {
			System.out.println("정방 행렬만 행렬식을 구할 수 있습니다.");
		}
		boolean[] isHidden = new boolean[col]; //가려진 열을 표시
		
		return determinant(0,0,row, isHidden); //0행0열 부터 size가 row인 소행렬 식을 구해서 반환한다.
	}
	
	private RationalNum determinant(int startRow, int startCol,int size, boolean[] isHidden) {//size*size 크기의 소행렬식을 구함
		if(size == 1) {//종결 조건
			return matrix[startRow][startCol];
		}
		
		RationalNum result = new RationalNum();//결과를 저장
		
		int curCol = startCol; //startCol은 가려지지 않은애만 들어 올 수 있음
		for(int i=0; i<size; i++) {//size번의 소행렬식을 구해 더해야함
			isHidden[curCol] = true; //현재 열 가림
			
			int c = 0; //시작 점을 찾기 위함 
			while(isHidden[c]) {//안 가린 애중 가장 작은 col부터 시작 해야함
				c++;
			}
			
			if(i%2 == 0){//홀수번째면 더함
				result.add(RationalNum.multiply(matrix[startRow][curCol], determinant(startRow+1,c,size-1,isHidden)));
				//System.out.println(result);
			}else {//짝수번째면 뺌
				result.subtract(RationalNum.multiply(matrix[startRow][curCol], determinant(startRow+1,c,size-1,isHidden)));
				//System.out.println(result);
			}
			
			isHidden[curCol] = false; //가림 해제
			
			curCol = nextCol(curCol,isHidden); //다음 가리지 않은 열을 찾음
		}
		return result;
	}
	
	private int nextCol(int curCol,boolean[] isHidden) {//가려지지 않은 다음 열을 찾아 주는 함수
		int nextCol = curCol+1;
		
		while(nextCol < col) {
			if(!isHidden[nextCol]) {//가려 지지 않았다면
				return nextCol;
			}
			nextCol++;
		}
		
		return -1; //다음 열 없음
	}

	void print() {
		for(int r=0; r<row ; r++) {
			for(int c=0; c<col; c++) {
				System.out.print(matrix[r][c] + "\t");
			}
			System.out.println();
		}
	}
}
