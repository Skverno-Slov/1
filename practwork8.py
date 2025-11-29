import math

def gauss_quadrature_integral(f, a, b, n):
    """
    Вычисление интеграла функции f на отрезке [a, b]
    с использованием квадратурной формулы Гаусса с двумя узлами
    """
    h = (b - a) / n
    integral = 0.0
    
    node1 = -1 / math.sqrt(3)
    node2 = 1 / math.sqrt(3)
    
    for i in range(n):
        xi = a + i * h
        mid_point = xi + h / 2
        
        point1 = mid_point + (h / 2) * node1
        point2 = mid_point + (h / 2) * node2
        
        integral += (h / 2) * (f(point1) + f(point2))
    
    return integral

def f(x):
    return 3 * x * math.exp(math.sin(x))

a = 0.3
b = 1.3

print("Вычисление интеграла функции f(x) = 3x ∙ e^(sin x)")
print(f"Интервал: [{a}, {b}]")
print()

n_values = [2, 4, 8, 16, 32]

for n in n_values:
    result = gauss_quadrature_integral(f, a, b, n)
    print(f"n = {n}: {result:.10f}")

result_high_precision = gauss_quadrature_integral(f, a, b, 100)
print(f"\nВысокая точность (n=100): {result_high_precision:.10f}")
