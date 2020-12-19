import time
from pyeasyga import pyeasyga
from math import sqrt
from colored import fg, bg, attr
import matplotlib.pyplot as plt

data = [0, 0, 1, 1, 0, 1, 1, 1, 0, 1, 0, 0, 1, 1, 0, 1, 1, 1, 0, 1, 0, 0, 1, 1, 0, 1, 1, 1, 0, 1, 0, 0, 1, 1, 0, 1, 1,
        1, 0, 1, 0, 0, 1, 1, 0, 1, 1, 1, 0, 1, 0, 0, 1, 1, 0, 1, 1, 1, 0, 1, 0, 0, 1, 1, 0, 1, 1, 1, 0, 1, 0, 0, 1, 1,
        0, 1, 1, 1, 0, 1, ]


labirynt = [
    1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
    1, 2, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1,
    1, 1, 1, 0, 0, 0, 1, 0, 1, 1, 0, 1,
    1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1,
    1, 0, 1, 0, 1, 1, 0, 0, 1, 1, 0, 1,
    1, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 1,
    1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1,
    1, 0, 1, 0, 0, 1, 1, 0, 1, 0, 0, 1,
    1, 0, 1, 1, 1, 0, 0, 0, 1, 1, 0, 1,
    1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1,
    1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 3, 1,
    1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
]
dlLabiryntu = 12
szLabiryntu = 12

s = 13
start = labirynt[s]

e = 130
exit = labirynt[e]
meta = e
infinity = meta * 2
'''


labirynt = [
	1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
	1,2,0,1,1,1,1,0,0,1,1,1,1,1,1,1,1,0,0,0,0,1,1,
	1,0,0,0,0,1,1,0,0,0,1,1,1,1,1,1,0,0,0,0,0,0,1,
	1,0,1,0,1,0,0,0,1,0,0,0,0,0,0,0,0,0,1,1,1,0,1,
	1,0,1,0,1,0,0,0,1,0,0,0,0,0,0,0,0,0,1,1,1,0,1,
	1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,1,1,0,1,
	1,1,1,1,1,0,1,1,0,0,1,0,0,0,0,1,1,1,0,0,1,0,1,
	1,0,0,1,1,0,0,0,0,1,1,0,0,1,1,1,1,1,1,0,0,1,1,
	1,0,0,0,0,1,1,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,1,
	1,0,0,1,1,1,1,0,1,1,1,0,0,1,0,0,0,0,0,1,1,1,1,
	1,1,0,1,1,1,1,0,0,1,1,0,0,1,1,1,1,0,0,1,1,1,1,
	1,0,0,0,0,1,1,0,0,0,1,0,0,1,1,0,0,0,1,0,0,1,1,
	1,0,1,1,1,1,1,1,1,0,1,0,0,0,1,1,0,0,0,1,1,1,1,
	1,1,1,1,1,1,1,1,0,0,0,0,0,1,1,1,0,0,1,1,0,0,1,
	1,0,0,1,1,0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,1,1,
	1,0,0,1,1,0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,1,1,
	1,0,0,1,1,0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,1,1,
	1,0,1,1,1,1,1,0,1,0,1,0,0,0,0,0,0,0,0,0,0,3,1,
	1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
]

dlLabiryntu = 23
szLabiryntu = 21

s = 24
start = labirynt[s]

e = 412
exit = labirynt[e]
meta = e
infinity = meta * 2

'''
def odleglosc(pozycja):
    punktX = int(pozycja % dlLabiryntu) - (dlLabiryntu - 2)
    punktY = int(pozycja / dlLabiryntu) - (szLabiryntu - 2)

    print("Punkt x: " + str(punktX) + " , y: " + str(punktY))

    return punktX + punktY

def liniaProsta(pozycja):
    a = int(pozycja % dlLabiryntu) - (dlLabiryntu - 2)
    b = int(pozycja / dlLabiryntu) - (szLabiryntu - 2)
    c = int(sqrt(a ** 2 + b ** 2))
    return c

def ruch(a, b):
    krok = 0
    if a == 0 and b == 0:  			#w lewo
        krok = -1
    if a == 0 and b == 1:  			#w prawo
        krok = 1
    if a == 1 and b == 0:  			#w gore
        krok = dlLabiryntu * -1
    if a == 1 and b == 1:  			#w dol
        krok = dlLabiryntu

    return krok

def kolejnaPozycja(obecnaPozycja, krok):
    nextKrok = obecnaPozycja + krok

    if (nextKrok < 0) or (nextKrok > len(labirynt) - 1) or (labirynt[nextKrok] == 1):
        krok = 0

    return obecnaPozycja + krok

def kP(obecnaPozycja, krok):
    nextKrok = obecnaPozycja + krok

    if (nextKrok < 0) or (nextKrok > len(labirynt) - 1):
        krok = 0

    return obecnaPozycja + krok

def fitness1(individual, data):
    pozycja = s
    for x in range(len(data)):
        if x % 2 == 0 or x == 0:
            if x == 0 or x % 2 == 0:
                krok = ruch(individual[x], individual[x + 1])

                if labirynt[pozycja + krok] == 1:
                    print ('%s%s SCIANA! %s' % (fg(1), bg(15), attr(0)))
                    print ("Pozycja: " + str(pozycja))
                    rezultat = odleglosc(pozycja)
                    print ("Odleglosc do exit: " + str(rezultat))
                    wLinii = liniaProsta(pozycja)
                    print ("W linii prostej do exit: " + str(wLinii))
                    print (" ")
                    return rezultat
                pozycja = kP(pozycja, krok)

                if pozycja == e:
                    print ("Sukces!")
                    print(individual)
                    print (" ")
                    return meta

    if pozycja == e:
        print ("Sukces")
        print (individual)
        print (" ")
        return meta

    return meta

def fitness2(individual, data):
    pozycja = s

    for x in range(len(data)):
        if x == 0 or x % 2 == 0:
            krok = ruch(individual[x], individual[x + 1])
            pozycja = kolejnaPozycja(pozycja, krok)

            if pozycja == e:
                print ('%s%s Sukces! %s' % (fg(46), bg(15), attr(0)))
                print(individual)
                print (" ",pozycja)
                return meta

    if pozycja == e:
        print ("Sukces")
        print (individual)
        print (" ")
        return meta


    print ("Pozycja: " + str(pozycja))
    rezultat = odleglosc(pozycja)
    print ("Odleglosc do exit: " + str(rezultat))
    wLinii = liniaProsta(pozycja)
    print ("W linii prostej do exit: " + str(wLinii))
    print (" ")

    return rezultat

def fitness3(individual, data):
    pozycja = s
    powtorzenia = 0

    for x in range(len(data)):
        if x == 0 or x % 2 == 0:
            p = pozycja
            krok = ruch(individual[x], individual[x + 1])
            pozycja = kolejnaPozycja(pozycja, krok)

            if p == pozycja:
                powtorzenia = powtorzenia - 2

            if labirynt[pozycja] == "+":
                powtorzenia = powtorzenia - 2

            if labirynt[pozycja] != 2:
                labirynt[pozycja] = "+"

            if pozycja == e:
                print ("Sukces!")
                print(individual)
                print (" ")
                return infinity

    if pozycja == e:
        print ("Sukces")
        print (individual)
        print (" ")
        return infinity
    print ("Pozycja: " + str(pozycja))
    rezultat = odleglosc(pozycja)
    print ("Odleglosc do exit: " + str(rezultat))
    wLinii = liniaProsta(pozycja)
    print ("W linii prostej do exit: " + str(wLinii))
    print (" ")

    return rezultat + powtorzenia

start_time = time.time()


ga1 = pyeasyga.GeneticAlgorithm(
    data,
    population_size = 40,
    generations = 20,
    mutation_probability = 0.05,
    elitism = True
)
ga2 = pyeasyga.GeneticAlgorithm(
    data,
    population_size = 40,
    generations = 20,
    mutation_probability = 0.05,
    elitism = True
)
ga3 = pyeasyga.GeneticAlgorithm(
    data,
    population_size = 40,
    generations = 20,
    mutation_probability = 0.05,
    elitism = True
)

ga1.fitness_function = fitness1
ga2.fitness_function = fitness2
ga3.fitness_function = fitness3

'''
ga1.fitness_function = fitness1
ga2.fitness_function = fitness1
ga3.fitness_function = fitness1
'''
ga1.run()
ga2.run()
ga3.run()

a=ga1.best_individual()
b=ga2.best_individual()
c=ga3.best_individual()

czas = time.time() - start_time
#print (czas)

print ("Labirynt o wymiarach: " + str(dlLabiryntu) + " x " + str(szLabiryntu))
print ("Fitness v.1 " + str(ga1.best_individual()))
print ("Fitness v.2 " + str(ga2.best_individual()))
print ("Fitness v.3 " + str(ga3.best_individual()))

algorytm1 = []
algorytm2 = []
algorytm3 = []

for individual in ga1.last_generation():
    algorytm1.append(individual[0])
for individual in ga2.last_generation():
    algorytm2.append(individual[0])
for individual in ga3.last_generation():
    algorytm3.append(individual[0])

print(algorytm1, algorytm2, algorytm3)

plt.plot(algorytm1, 'ro-', label ='Fitness 1')
plt.plot(algorytm2, 'bo-', label ='Fitness 2')
plt.plot(algorytm3, 'go-', label ='Fitness 3')

plt.suptitle('Algorytm Genetyczny \n pop = 10 gen = 20 mut = 5% el = True')
#plt.suptitle('Algorytm Genetyczny \n pop = 40 gen = 20 mut = 1% el = True')
#plt.suptitle('Algorytm Genetyczny \n pop = 60 gen = 20 mut = 0,5% el = True')
'''
plt.plot(algorytm1, 'ro-', label ='pop=10\n mut=5%')
plt.plot(algorytm2, 'bo-', label ='pop=40\n mut=1%')
plt.plot(algorytm3, 'go-', label ='pop=60\n mut=0,5%')
plt.suptitle('Algorytm Genetyczny Fitness v1')
'''
plt.xlabel('pokolenie')
plt.ylabel('fitness(ocena)')
plt.legend()
plt.show()
'''
best =ga1.best_individual()
p = s
i = 0
w=p
for x in range(40):
  k = ruch(best[1][i], best[1][i + 1])
  w = kolejnaPozycja(w,k)
  if w !=s and w != e:
    labirynt[w] = "+"
  i+=2

i = 0;
for el in labirynt:
  if(el=="+"):
      print(el,end="")
  elif(el==0):
      print(el,end="")
  elif(el==2):
    print (el,end="")
  else:
    print(el,end="")

  i = i + 1
  if i % dlLabiryntu == 0:
    print()
'''