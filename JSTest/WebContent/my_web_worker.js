var end = 1e8, tmp = 1;

postMessage('�ȳ��ϼ���.');

while (end) {
	end -= 1;
	tmp += end;
	
	if (end === 5e7) {
		postMessage('�������� ����Ǿ����ϴ�.' + tmp);
	}
}

postMessage('�۾� ����');