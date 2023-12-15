import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

@SpringBootApplication
public class LinearEquationSolverApplication {

    public static void main(String[] args) {
        SpringApplication.run(LinearEquationSolverApplication.class, args);
    }
}

@Controller
class LinearEquationController {

    @PostMapping("/solve")
    public String solveEquation(@RequestParam int a, @RequestParam int b, @RequestParam int c,
                                @RequestParam int d, @RequestParam int p, @RequestParam int n,
                                Model model) {
        String[] results = calculateValues(a, b, c, d, p, n);
        model.addAttribute("results", results);
        return "index";
    }

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    private String[] calculateValues(int a, int b, int c, int d, int p, int n) {
        if (a * d - b * c == 0 || Math.abs(d) - Math.abs(b) != 1 || gcd(a * d - b * c, p) != 1) {
            return new String[]{"ERROR", null};
        }

        int X = modInverse(a * d - b * c, p) * ((d % p) + (-b % p)) % p;
        int Y = modInverse(a * d - b * c, p) * ((-c % p) + (a % p)) % p;

        for (int i = 0; i < Math.log(n) / Math.log(2); i++) {
            int X1 = X * (2 + X * (c * b - a * d));
            int Y1 = Y - X * (d * a * Y - b * c * Y + c - a);
            X = X1;
            Y = Y1;
        }

        return new String[]{
                String.format("X = %d + (%d^%d) * m = %d + (%d * m)", X, p, n, X + (int) Math.pow(p, n)),
                String.format("Y = %d + (%d^%d) * m = %d + (%d * m)", Y, p, n, Y + (int) Math.pow(p, n))
        };
    }

    private int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    private int modInverse(int a, int m) {
        a = a % m;
        for (int x = 1; x < m; x++)
            if ((a * x) % m == 1)
                return x;
        return 1;
    }
}