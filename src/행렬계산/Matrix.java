package 행렬계산;

/*
 * 행렬을 나타내며
 * 다양한 행렬 계산 기능을 제공한다.
 */

public class Matrix{//행렬
	public static final int UNIQUE_SOLUTION = 1, COUNTLESS_SOLUTION = 2, NO_SOLUTION = 3; //연립 방정식 계산결과를 뜻하는 상수
	private RationalNum[][] matrix; //2차원 유리수 배열
	private int row; //행 크기
	private int col; //열 크기
	
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
			throw new RuntimeException("곱의 형태가 맞지 않습니다.");
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
		if(row != col) {//정방행렬이 아닌경우
			throw new RuntimeException("정방 행렬만 행렬식을 구할 수 있습니다.");
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
			throw new RuntimeException("정방 행렬만 역행렬을 구할 수 있습니다.");
		}

		if(determinant.equals(new RationalNum(1,0))) {//비가역 행렬인 경우
			throw new RuntimeException("비가역 행렬입니다.");
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

	/*
	 * 역행렬을 이용해 연립 방정식의 해를 행렬 형태로 반환하는 함수
	 * (현재 행렬에 어떤 행렬을 곱해야 결과 행렬이 나오는지)
	 */

	public Matrix getSolutionByInverse(Matrix result) {
		if(row != result.row) {
			throw new RuntimeException("구조가 맞지 않습니다.");
		}

		return multiply(getInverse(),result);
	}
	
	/*
	 * 첨가 행렬과 미지수 개수를 받아서 해를 구한다.
	 * 만약 무수히 많은 해가 존재한다면 해공간의 기저벡터를 열벡터로 하는 행렬을 반환한다.
	 * 해가 없다면 null을 반환 한다.
	 * 결과 resultBox에 어떤 결과가 나왔는지 넣어준다.
	 * 무수히 많은 해를 벡터 방정식으로 표현해야 한다면 soultionBox에 벡터 방정식을 넣어서 반환
	 */
	static Matrix getSolutionByGauss(Matrix m, int col, int[] resultBox, String[] solutionBox) {
		
		RationalNum[][] matrix = new RationalNum[m.row+1][m.col+1]; 
		
		//각행의 마지막 1열은 해당 행에서 leading변수를 가진 열이 있다면 그열을 표시 없다면 null
		//각열의 마지막 1행은 행당 열의 leading변수를 가진 행이 있다면 그행을 표시 없다면 null
		int rankA = 0; //rank(A)
		resultBox[0] = UNIQUE_SOLUTION; //일단 유일해로 간주
		RationalNum[][] rowAdress = new RationalNum[col][]; //idx열의 leading변수가있는 행의 주소를 저장
		
		//copy
		for(int r=0; r<m.row; r++) {
			for (int c =0; c <m.col; c++){
				matrix[r][c] = new RationalNum(m.matrix[r][c].getMom(), m.matrix[r][c].getSon());
			}
		}
		
		for(int startC=0; startC<col; startC++) {//leading변수를 만들고 싶은 열
			//leading 1을 가질 행 골라서 0행과 교환한다.
			boolean flag = false;
			for(int r=0; r<m.row; r++) {
				if(matrix[r][startC].getSon() !=0 && matrix[r][m.col] == null) {

					matrix[r][m.col] = matrix[r][startC]; //그 행의 leading변수를 가진 실수객체의 주소를 마지막 열에 저장
					matrix[m.row][startC] = matrix[r][startC]; //그 열의 leading변수를 가진 실수객체의 주소를 마지막 행에 저장
					
					//선택한 행을 맨 위로 올림
					rowAdress[startC] = matrix[r];
					matrix[r] = matrix[0];
					matrix[0] = rowAdress[startC];
					
					rankA++; //rank증가
					
					flag = true;
					break;
				}
			}
			
			if(!flag) {
				resultBox[0] = COUNTLESS_SOLUTION;//해가 없는 경우 일 수도 있으나 일단 자유변수를 가지는 것으로 간주
				break;
			}
			
			for(int r=0; r<m.row; r++) {//startC 열을 1로 맞춤
				if(!((matrix[r][startC].getSon() == 1 && matrix[r][startC].getMom() == 1)|| matrix[r][startC].getSon() == 0)) { //1이나 0이 아니면
					int toSon = matrix[r][startC].getMom(); //분자에 곱해야 할 값
					int toMom = matrix[r][startC].getSon(); //분모에 곱해야 할 값

					for(int c=0; c<m.col; c++) {
						matrix[r][c].setSon(matrix[r][c].getSon() * toSon);
						matrix[r][c].setMom(matrix[r][c].getMom() * toMom);
					}	
				}
			}
			

			for(int r=1; r<m.row; r++) {//밑에 행에 모두 뺀다.
				if(matrix[r][startC].getSon() != 0) {//startC 열이 0이 아닌 행에는 0행을 빼준다.
					for(int c=startC; c<m.col; c++) {
						matrix[r][c].subtract(matrix[0][c]);
					}
				}
			}

		}
		
		for(int r=0; r<m.row; r++) {
			if(matrix[r][m.col] == null) {//자유변수가 없는 행인데
				for(int c=col; c<m.col; c++) { //B가 영행이 아니라면 
					if(matrix[r][c].getSon() != 0) {//해가없는 경우, 000|상수 꼴
						resultBox[0] = NO_SOLUTION;
						//System.out.println("해가 없다.");
						return null;
					}
				}		
			}
		}
		
		//rank(A) == rank(A|B) 이므로 해를 가짐
		RationalNum[][] solution;
		if(resultBox[0] == UNIQUE_SOLUTION) {//유일 해를 가지는 경우
			solution = new RationalNum[col][m.col - col]; //A|B의 열수 - A의 열수 = B의 열수	
			for(int c=0; c<col; c++) {
				for(int i=0; i<solution[0].length; i++) {
					solution[c][i] = new RationalNum(RationalNum.divide(rowAdress[c][col+i],matrix[m.row][c]));
					solution[c][i].makeSimple();//약분
				}
			}
			return new Matrix(solution);
			
		}else {//무수히 많은 해를 가지는 경우		
			if(m.col - col != 1) {//B가 1열이 아닌 경우
				solutionBox[0] = new String("B가 1열이 아닌 경우는 행공간을 구할 수 없습니다.");
				return null;
			}
			
			StringBuffer solutionStr = new StringBuffer();
			int numV = 0; //자유변수 번호
			for(int oC = 0; oC < col; oC++) {
				
				if(matrix[m.row][oC] == null) {//해당열은 자유변수를 가짐
					//자유변수로 각 행의 변수를 표현하여 ( , , , col만큼) 꼴로 만들어야함
					solutionStr.append("( ");
					for(int iC = 0; iC < col; iC++ ) {
						//iC열이 선행변수를 가지면 현재 자유변수로 표현
						if(iC == oC) { //자유변수 잡고 있는 열은 1
							solutionStr.append("1 ");
						}else if(matrix[m.row][iC] != null) {
							RationalNum value = new RationalNum(rowAdress[iC][oC]);//현재 선행변수를 가지는 행의 자유변수열의 수
							value.multiply(-1); //이항
							value.divide(matrix[m.row][iC]); //자유변수로 나눔
							value.makeSimple();//약분
							
							solutionStr.append(value.toString() + " ");
						}else { //0이 됨
							solutionStr.append("0 ");
						}
					}
					
					solutionStr.append(")X" + numV++ + " + ");
				}
			}
			
			//마지막 상수항 처리
			solutionStr.append("( ");
			for(int oC = 0; oC < col; oC++) {
				if(matrix[m.row][oC] != null) { //leadig 변수를 가진다면
					//해당 열의 leading변수를 가지는 행의 -상수항을 성분으로 가짐
					//solutionStr.append(RationalNum.multiply(rowAdress[oC][col],new RationalNum(1,-1)).toString() + " ");
					rowAdress[oC][col].makeSimple();
					solutionStr.append(rowAdress[oC][col].toString() + " ");
				}else {
					solutionStr.append("0 ");
				}
			}
			solutionStr.append(" )");
			
			//저장
			solutionBox[0] = solutionStr.toString();
			return null;			
		}		
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
