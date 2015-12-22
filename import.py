import requests
from bs4 import BeautifulSoup
import sys

res = requests.get('http://adventofcode.com/day/' + sys.argv[1], cookies = {'session':'53616c7465645f5fef2d460d09fc69d2f5f6d751a69109c4011474ca0a115c0adc365989982e17b990af7ffd5aa5ecc8'})
content =  res.text
soup = BeautifulSoup(content, 'html.parser')

def unparse_list(sep, tag):
    return sep.join((unparsed for item in tag for unparsed in unparse(item)))

def unparse(tag):
    if tag.name == 'h2':
        yield '## ' + unparse_list('', tag) + '\n'
    elif tag.name == 'p':
        yield  unparse_list('', tag) + '\n'
    elif isinstance(tag, basestring):
        yield  tag
    elif tag.name == 'em':
        yield  '*' + unparse_list('', tag) + '*'
    elif tag.name == 'code':
        yield  '`'+ unparse_list('', tag) + '`'
    elif tag.name == 'span':
        yield  unparse_list('', tag) 
    elif tag.name == 'ul':
        for li in tag:
            for unparsed in unparse (li):
                yield unparsed
    elif tag.name == 'li':
        yield  ' - ' + unparse_list('', tag) 
    elif tag.name == 'pre':
        yield  '```\n' 
        for item in tag:
            for unparsed in unparse (item):
                yield unparsed
        yield  '```\n' 
        
    elif tag.name == 'a':
        yield '['+unparse_list('', tag)+']('+tag.get('href')+')'
    else:
        raise Exception(tag.name)

for article in soup.findAll("article"):
    print unparse_list('', article)

# //7 9 13 15 16 18