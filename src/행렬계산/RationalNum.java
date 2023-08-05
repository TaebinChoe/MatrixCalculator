package 행렬계산;

/*
 * 유리수 를 나타내는 클래스이다.
 */

class  RationalNum{
	private int mom; //분모
	private int son; //분자
	
	
	public RationalNum(){ //기본 값
		mom = 1;
		son = 0;
	}
	
	public RationalNum(int n){
		mom = 1;
		son = n;
	}
	
	public RationalNum(int mom, int son) {
		this.mom = mom;
		this.son = son;
		makeSimple(); //기본적으로 기약분수로 만들어둠
	}
	
	public RationalNum(RationalNum r){
		this.mom = r.mom;
		this.son = r.son;
	}
	
	public int getMom() {//분모를 반환
		return mom;
	}
	
	public int getSon() {//분자를 반환
		return son;
	}
	
	public void setMom(int mom) {
		this.mom = mom;
	}
	
	public void setSon(int son) {
		this.son = son;
	}
	
	public boolean equals(Object o){
		if(!(o instanceof RationalNum)) {
			return false;
		}

		//x 자로 곱해서 같은지
		return (((RationalNum)o).mom *son == ((RationalNum)o).son*mom );	
	}

	public void makeSimple() {//약분한다.
		if(son == 0){//0의 기본 형은 0/1이다.
			mom = 1; 
			return;
		}

		if(son<0) { //분자가 0보다 큰 상황으로 통일 한다.
			son *= -1;
			mom *= -1;
		}

		for(int divisor=1; divisor<=son; divisor++) {
			if(son % divisor == 0 && mom % divisor == 0) {
				son /= divisor;
				mom /= divisor;
				divisor = 1;
			}
		}
		
		if(mom < 0) {//분자가 음수 인 형태로 맞추자
			son *= -1;
			mom *= -1;
		}
	}
	
	//단항 연산자
	void add(RationalNum f) {
		RationalNum result = add(this,f);
		mom = result.mom;
		son = result.son;
	}
	
	void add(int n) {
		add(new RationalNum(1,n));
	}
	
	void subtract(RationalNum f) {
		RationalNum result = subtract(this,f);
		mom = result.mom;
		son = result.son;
	}
	
	void subtract(int n) {
		subtract(new RationalNum(1,n));
	}
	
	void multiply(RationalNum f) {
		RationalNum result = multiply(this,f);
		mom = result.mom;
		son = result.son;
	}
	
	void multiply(int n) {
		multiply(new RationalNum(1,n));
	}
	
	void divide(RationalNum f) {
		RationalNum result = divide(this,f);
		mom = result.mom;
		son = result.son;
	}
	
	void divide(int n) {
		divide(new RationalNum(1,n));
	}
	
	//양항 연산자
	public static RationalNum add(RationalNum f1, RationalNum f2) {
		return new RationalNum(f1.mom*f2.mom, f1.son*f2.mom + f2.son*f1.mom);	
	}
	
	public static RationalNum subtract(RationalNum f1, RationalNum f2) {
		return add(f1,new RationalNum(f2.mom, -f2.son));
	}
	
	public static RationalNum multiply(RationalNum f1, RationalNum f2) {
		return new RationalNum(f1.mom * f2.mom, f1.son * f2.son);
	}
	
	public static RationalNum divide(RationalNum f1, RationalNum f2) {
		return new RationalNum(f1.mom * f2.son, f1.son * f2.mom);
	}
	
	public String toString() {
		if(son == 0) {
			if(mom!=0) {
				return "0";
			}else {
				return "?"; //부정 형 0/0
			}	
		}else if(mom == 0){//무한대일때
			return "∞";
		}else if(mom == 1){//정수일때
			return son + "";
		}else {//정수가 아닌 유리수 일때
			return son + "/" + mom;
		}
	}
	
}
