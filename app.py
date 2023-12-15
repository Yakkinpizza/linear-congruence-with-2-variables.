from flask import Flask, render_template, request
import math

app = Flask(__name__)

def calculate_values(a, b, c, d, p, n):
    fnd = 0
    if a * d - b * c == 0 or abs(d) - abs(b) != 1 or math.gcd(a * d - b * c, p) != 1:
        return "ERROR", None

    X = ((((a * d - b * c) % p) ** (p - 2)) * ((d % p) + (-b % p))) % p
    Y = ((((a * d - b * c) % p) ** (p - 2)) * ((-c % p) + (a % p))) % p

    for i in range(int(math.log2(n))):
        X1 = X * (2 + X * (c * b - a * d))
        Y1 = Y - X * (d * a * Y - b * c * Y + c - a)
        X = X1
        Y = Y1

    result_tuple = (
    f"X = {X} + ({p}^{n}) * m) = {X} + ({p ** n}m)",
    f"Y = {Y} + ({p}^{n}) * m) = {Y} + ({p ** n}m)"
    )



    return None, result_tuple

@app.route("/", methods=["GET", "POST"])
def index():
    if request.method == "POST":
        a, b, c, d = map(int, request.form["coefficients"].split())
        p = int(request.form["mod_value"])
        n = int(request.form["power_value"])

        error, results = calculate_values(a, b, c, d, p, n)

        return render_template("index.html", error=error, results=results)
    else:
        return render_template("index.html")

if __name__ == "__main__":
    app.run(debug=True)
