from selenium import webdriver
from BeautifulSoup import BeautifulSoup
import pandas as pd

driver = webdriver.Firefox()
driver.get("https://icobench.com/ieo")

l = []
data = []
p = []

content = driver.page_source
soup = BeautifulSoup(content)

a = soup.find('tbody')
b= a.findAll('td')
c = a.findAll('tr')

for th in c[0].findAll('th'):
    l.append(th.text.replace('\n', ' ').strip())

for td in b:
    data.append(td.text)
    if len(data) == 8:
        p.append(data)
        data = []

driver.close()

project = []
progress = []
launchpad = []
rating = []
token = []
start = []
rewiew = []

for x in range(len(p)):
    project.append(p[x][1])
    progress.append(p[x][2])
    launchpad.append(p[x][3])
    rating.append(p[x][4])
    token.append(p[x][5])
    start.append(p[x][6])
    if p[x][7] != "Coming soonPRO":
        rewiew.append("PDF")
    else:
        rewiew.append(p[x][7])

d = {'PROJECT': project, 'PROGRESS': progress, 'LAUNCHPAD': launchpad, 'RATING': rating, 'TOKEN': token, 'START': start,'REWIEW': rewiew}
df = pd.DataFrame.from_dict(d, orient='index')
df = df.transpose()
df.to_csv('products.csv', index=False, encoding='utf-8')




