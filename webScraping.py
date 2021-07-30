import time
from bs4 import BeautifulSoup
from selenium import webdriver
from selenium.webdriver.chrome.options import Options

options = Options()
options.add_argument('--headless')
#options.add_argument('--disable-gpu')
driver = webdriver.Chrome(chrome_options=options)
f = open("output.txt","a")
for i in range(49,99):
    url = "https://fdc.nal.usda.gov/fdc-app.html#/food-details/4671" + str(i) +"/nutrients"
    #url = "https://fdc.nal.usda.gov/fdc-app.html#/food-details/5779" + str(i) + "/nutrients"
    driver.get(url)
    time.sleep(3)
    page = driver.page_source
    #driver.quit()
    soup = BeautifulSoup(page, 'html.parser')
    container = soup.find(id='nutrients-table')
    #job_elems = container.find_all('td',class_='numeric')
    j=0
    titles = soup.find(id='foodDetailsDescription')
    print(titles.text.strip(), file=f, end = "~")
    job_elems = container.find_all('span',{'name':'finalFoodNutrientValue'})
    for job_elem in job_elems:
        if j < 4:
            print(job_elem.text.strip(), file = f, end = "~")
        else:
            j=10
        j+=1
    print(file = f)
driver.quit()