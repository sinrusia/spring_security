var end = 1e8, tmp = 1;

postMessage('안녕하세요.');

while (end) {
	end -= 1;
	tmp += end;
	
	if (end === 5e7) {
		postMessage('절반정도 진행되었습니다.' + tmp);
	}
}

postMessage('작업 종료');