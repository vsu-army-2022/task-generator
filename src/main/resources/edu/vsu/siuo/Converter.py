import xml.etree.ElementTree as xml
from xml.etree.ElementTree import tostring

with open('ts.txt', encoding='UTF-8') as file:
    text = file.readlines()

root = xml.Element('Таблица_стрельбы')
power = None
for line in text:
    if 'Полный' in line:
        power = xml.Element("Полный")
        root.append(power)
    elif 'Уменьшенный' in text:
        powerU = xml.Element("Уменьшенный")
        root.append(power)
    elif 'Первый' in line:
        power = xml.Element("Первый")
        root.append(power)
    elif 'Второй' in line:
        power = xml.Element("Второй")
        root.append(power)
    elif 'Третий' in line:
        power = xml.Element("Третий")
        root.append(power)
    elif 'Четвертый' in line:
        power = xml.Element("Четвертый")
        root.append(power)
    elif power is not None and not 'Дальн' in line:
        str = line.replace('\n', '')
        str = str.split("\t")
        if len(str) > 3:
            distance = xml.SubElement(power, "Дальность")
            distance.text = str[0]
            pr = xml.SubElement(power, "Пр")
            pr.text = str[1]
            x = xml.SubElement(power, "Xтыс")
            x.text = str[2]
            wd = xml.SubElement(power, "Вд")
            wd.text = str[3]
        else:
            continue

    #print(line)

tree = xml.ElementTree(root)
tree.write("ts.xml", encoding='UTF-8')