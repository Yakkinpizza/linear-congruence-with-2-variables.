from flask import Flask, render_template, request
import math

app = Flask(__name__)

def calculate_values(a, b, c, d, p, n):
    fnd = 0
    if a * d - b * c == 0 or abs(d) - abs(b) != 1:
        return "ERROR"

    X = ((((a * d - b * c) % p) ** (p - 2)) * ((d % p) + (-b % p))) % p
    Y = ((((a * d - b * c) % p) ** (p - 2)) * ((-c % p) + (a % p))) % p

    results = []
    for i in range(int(math.log2(n)) + 1):
        result_tuple = (
            X,
            Y,
            (a * X + b * Y) % (p ** (2 ** i)),
            (c * X + d * Y) % (p ** (2 ** i)),
        )
        results.append(result_tuple)

        X1 = X * (2 + X * (c * b - a * d))
        Y1 = Y - X * (d * a * Y - b * c * Y + c - a)

        X = X1
        Y = Y1

    return results

@app.route("/", methods=["GET", "POST"])
def index():
    if request.method == "POST":
        a, b, c, d = map(int, request.form["coefficients"].split())
        p = int(request.form["mod_value"])
        n = int(request.form["power_value"])

        results = calculate_values(a, b, c, d, p, n)

        return render_template("index.html", results=results)
    else:
        return render_template("index.html")

if __name__ == "__main__":
    app.run(debug=True)
