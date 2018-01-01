
import java.math.BigInteger;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner scanner=new Scanner(System.in);//手動輸入測資
		System.out.print("e=");
		String E=scanner.nextLine();
		System.out.print("p=");
		String P=scanner.nextLine();
		System.out.print("q=");
		String Q=scanner.nextLine();
		System.out.print("text=");
		String M=scanner.nextLine();
		BigInteger p=new BigInteger(P);
		BigInteger q=new BigInteger(Q);
		BigInteger e=new BigInteger(E);
		RSA rsa=new RSA(p,q,e,encoding(M));
		System.out.println("M=\n"+rsa.getM());
		
		System.out.println("C=\n"+rsa.getC());
		System.out.println("d=\n"+rsa.getD());
		System.out.println("After decoding:\n"+rsa.getM());
	}

	static String encoding(String strings){
		String output="";
		if(strings.length()%2!=0)strings+=" ";
		char[] arr=strings.toCharArray();
		
		for(int i=0;i<arr.length-1;i+=2){
			int n=0;
			if(arr[i]==' ')n+=26;
			else n+=(int)(arr[i]-'A');
			n*=100;
			if(arr[i+1]==' ')n+=26;
			else n+=(int)(arr[i+1]-'A');
			
			output+=(n+" ");
		}
		
		return output;
	}
}

class RSA {
	private BigInteger e;
	private String M;
	private BigInteger n;
	private BigInteger phy;
	private BigInteger d,y;
	public RSA(BigInteger p,BigInteger q,BigInteger e,String M){
		setN(p, q);
		setPhy(p, q);
		this.e=e;
		this.M=M;
	}
	public BigInteger getE(){
		return e;
	}
	public BigInteger getN(){
		return n;
	}
	public BigInteger getD(){
		e_gcd(e, phy, d, y);
		return d;
	}
	public String getM(){
		return M;
	}
	public String getC(){
		return RSAencoding();
	}
	private void setN(BigInteger p,BigInteger q){
		n= p.multiply(q);//p*q
	}
	private void setPhy(BigInteger p,BigInteger q){
		phy= p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));//(p-1)*(q-1)
	}
	private String RSAencoding(){
		String C="";
		Scanner scanner=new Scanner(M);
		while(scanner.hasNext())
			C+=(new BigInteger(scanner.next()).modPow(e, n)+" ");//(M^e)mod(n)
		return C;
	}
	public String RSAdecoding(String C){
		String output="";
		Scanner scanner=new Scanner(C);
		while(scanner.hasNext())
			output+=(new BigInteger(scanner.next()).modPow(d, n)+" ");//(C^e)mod(n)
		return output;
	}
	private BigInteger e_gcd(BigInteger a,BigInteger b,BigInteger d,BigInteger y){
		if(b==BigInteger.ZERO){
			this.d=BigInteger.ONE;
			this.y=BigInteger.ZERO;
			return a;
		}
		else{
			BigInteger ans=e_gcd(b, a.mod(b), this.d, this.y);
			BigInteger tmp=this.d;
			this.d=this.y;
			this.y=tmp.subtract(a.divide(b).multiply(this.y));
			return ans;
		}
	}
}
