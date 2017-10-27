workdir = r'C:\Users\Piotr\GitHubProjects\clustering\results'
workdir_quality = workdir + r'\quality'
workdir_efficiency = workdir + r'\efficiency'

results_file = open(workdir_quality + r'\files.txt', encoding='utf-8')
silhouettes_file = open(workdir_quality + r'\silhouettes.txt', encoding='utf-8')
times_file = open(workdir_quality + r'\times.txt', encoding='utf-8')
times_file_efficiency = open(workdir_efficiency + r'\times.txt', encoding='utf-8')
pdf_file = open(workdir + r'\experiments.tex', 'w', encoding='utf-8')

files = results_file.readlines()
silhouettes = silhouettes_file.readlines()
times = times_file.readlines()

html = '<h1>Clustering with constraints (NBC & DBSCAN)</h1>'

html += '<h2>Testing quality</h2>'

line = 0

row = 0
col = 0
f = 0

def get_dataset(line):
    file = files[line].strip()
    dataset = file.split('_')[0]
    return dataset

def get_pic(line):
    file = files[line].strip()
    pref = file.replace(r'.csv', '')
    name = '{"' + workdir_quality + '\\' + pref + '"}' + r'.png'
    name = name.replace('\\','/')
    name = '\\includegraphics[width=3.5cm]{' + name + '}'
    return name
    
def get_silh(line):
    silhouette = silhouettes[line]
    silhouette = silhouette.strip().split('\t')[0]
    silhouette = silhouette.replace('\ufeff', '')
    silhouette = "{0:.3f}".format(float(silhouette))
    #if float(silhouette) == -1.0:
    #    return ""
    #else:
    return 's='+silhouette

def get_alg(line):
    alg = times[line].strip()
    alg = alg.split('\t')[0]
    return alg

def get_params(line):
    params = times[line].strip()
    params = params.split('\t')[1]
    params = params.replace('(', '')
    params = params.replace(')', '')
    return params

def get_ind_time(line):
    params = times[line].strip()
    params = params.split('\t')[2]
    params = params.replace('(', '')
    params = params.replace(')', '')
    return params

def get_clu_time(line):
    params = times[line].strip()
    params = params.split('\t')[3]
    params = params.replace('(', '')
    params = params.replace(')', '')
    return params

def get_def_time(line):
    params = times[line].strip()
    params = params.split('\t')
    
    if params[0].startswith('C-'):
        return params[4]
    else:
        return ''

def get_clu_count(line):
    params = times[line].strip()
    params = params.split('\t')
    print('-'.join(params))
    if params[0].startswith('NBC'):
        return params[5]
    elif params[0].startswith('C-NBC'):
        return params[6]
    elif params[0].startswith('DBSCAN'):
        return params[5]
    elif params[0].startswith('C-DBSCAN'):
        return params[6]
    else:
        return 'x'

def get_con_num(line):
    l = line % 6
    if l == 0:
        return str(10)
    else:
        return str(20*l)
        
datasets = ['birch1', 'birch2', 'birch3']

tab1 = ''

pdf = ''
pdf += '\\title{}\n'
pdf += '\\date{}\n'
pdf += '\\documentclass[12pt]{article}\n'
pdf += '\\usepackage{graphicx}\n'
pdf += '\\usepackage{subcaption}\n'
pdf += '\\usepackage{array}\n'
pdf += '\\newcolumntype{x}[1]{>{\\centering\\arraybackslash\\hspace{0pt}}p{#1}}\n'
pdf += '\\begin{document}\n'
pdf += '\\maketitle\n'
pdf += '\\section{Experiments}\n'

for i in range(0,6):
    
    a = get_alg(i*6)
    ca = get_alg(i*6+3)
    
    pdf += '\n\\begin{figure}\n'
    pdf += '\centering\n'
    #pdf += '\hline\n'
        
    for x in range(0,2):

        for j in range(x,6,2):
            l = i*6 + j
            sil = get_silh(l)
            clu = get_clu_count(l)
            dat = get_dataset(l)
            det = get_def_time(l)
            clt = get_clu_time(l)
            pic = get_pic(l)
            alg = get_alg(l)
            par = get_params(l)
            #print(fil, sil, alg, par, clu, det, clt)

            pdf += '\\begin{subfigure}[b]{0.31\\textwidth}\n'
            pdf += pic + '\n'
            par = par.replace('=', ' = ')
            sil = sil.replace('=', ' = ')
            pdf += '\\caption{' + par + ', ' + sil + ',' + det + '}\n'
            pdf += '\\end{subfigure}\n'
            pdf += '~\n'
            if x > 0 and x % 2 == 0:
                pdf += '\\newline\n'
            
    pdf += '\caption{'+ a + ', ' + ca +'}\n'    
    pdf += '\end{figure}\n'
    #pdf += '\end{table}\n\n'


# ------------------------------------------

tab = ''
tab += '\\begin{table}\n'
tab += '\caption{Quality}\n'
tab += '\\label{my-label}\n'
tab += '\\resizebox{\\textwidth}{!}{\n'
tab += '\\begin{tabular}{p{2cm}p{4cm}p{6cm}cx{2cm}cx{2cm}cx{2cm}cx{2cm}cx{2cm}}\n'
tab += '\\hline\n'
tab += '\\hline\n'
tab += 'Dataset & Algorithm & Parameters & Ind. & Clust. & Def. & Count & Silh. \\\\ \hline \n'
tab += '\\hline\n'
for l in range(0,36):
    sil = get_silh(l)
    clu = get_clu_count(l)
    dat = get_dataset(l)
    det = get_def_time(l)
    clt = get_clu_time(l)
    pic = get_pic(l)
    alg = get_alg(l)
    par = get_params(l)
    ind = get_ind_time(l)
    sil = get_silh(l)
    
    par = par.replace('=', ' = ')
    sil = sil.replace('s=', '')
    cols = []
    cols.append(dat)
    cols.append(alg)
    cols.append(par)
    cols.append(ind)
    cols.append(clt)
    cols.append(det)
    cols.append(clu)
    cols.append(sil)
    
    tab += '\t&\t'.join(cols) + ' \\\\ ' + '\n'
    if (l+1) % 6 == 0:
        tab += '\\hline\n'

    #if (l+1) % 2 == 0:
    #    tab += '\\hline\n'

tab += '\\hline\n'
tab += '\\end{tabular}}\n'
tab += '\\end{table}\n'
tab += '\n'

pdf += tab

print('\n\n\n')

results_file = open(workdir_efficiency + r'\files.txt', encoding='utf-8')
times_file = open(workdir_efficiency + r'\times.txt', encoding='utf-8')

files = results_file.readlines()
times = times_file.readlines()

tab = ''
tab += '\\begin{table}\n'
tab += '\caption{Efficiency}\n'
tab += '\\label{my-label}\n'
tab += '\\resizebox{\\textwidth}{!}{\n'
tab += '\\begin{tabular}{p{2cm}p{4cm}p{6cm}cx{2cm}cx{2cm}cx{2cm}cx{2cm}cx{2cm}}\n'
tab += '\\hline\n'
tab += '\\hline\n'
tab += 'Dataset & Algorithm & Parameters & Const. & Count & Ind. & Clust. & Def. & Total \\\\ \hline \n'
tab += '\\hline\n'
for l in range(0,36):
    clu = get_clu_count(l)
    dat = get_dataset(l)
    det = get_def_time(l)
    clt = get_clu_time(l)
    pic = get_pic(l)
    alg = get_alg(l)
    par = get_params(l)
    ind = get_ind_time(l)
    con = get_con_num(l)
    tot = int(ind) + int(det) + int(clt)
    
    par = par.replace('=', ' = ')
    sil = sil.replace('s=', '')
    cols = []
    cols.append(dat)
    cols.append(alg)
    cols.append(par)
    cols.append(con)
    cols.append(clu)
    cols.append(ind)
    cols.append(clt)
    cols.append(det)
    cols.append(str(tot))
    
    tab += '\t&\t'.join(cols) + ' \\\\ ' + '\n'
    if (l+1) % 6 == 0:
        tab += '\\hline\n'

    #if (l+1) % 2 == 0:
    #    tab += '\\hline\n'

tab += '\\hline\n'
tab += '\\end{tabular}}\n'
tab += '\\end{table}\n'
tab += '\n'

print('\n\n\n')

pdf += tab
pdf += '\\end{document}\n'

print(pdf)



"""
for file in files:
  
    time = times[line]
    picpath = workdir + '\\' + name
    img = '<img src="' + picpath + '">'

    html += pref + '<br>'
    html += silhouette + '<br>'

    time = time.strip().split('\t')[0]
    html += silhouette + '<br>'
    
    html += img + '<br>'
    line += 1
    
    col += 1
    col = col % 4
html += '<h2>Testing efficiency</h2>'
"""

"""tab = open(workdir + r'\tab.txt', encoding='utf-8')
lines = tab.readlines()

html += '<table border=1>'
for line in lines:
    html += "<tr>"
    line = line.strip()
    cols = line.split('\t')
    for col in cols:
        html += "<td>"
        html += col
        html += "</td>"
    html += "</tr>"
html += '</table>'
"""

html_file = open(workdir + r'\index.html', 'w', encoding='utf-8')
html_file.write(html)
pdf_file.write(pdf)
pdf_file.close()
html_file.close()
silhouettes_file.close()
times_file.close()
results_file.close()
times_file_efficiency.close()