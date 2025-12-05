import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

void main(){

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    String password = bCryptPasswordEncoder.encode("asd");
    String password1 = bCryptPasswordEncoder.encode("zxc");
    System.out.println(password);
    System.out.println(password1);
}