/* festival.js */
let cd=new Date();
let selectedDate=`${cd.getFullYear()}${(cd.getMonth()+1).toString().padStart(2,'0')}${cd.getDate().toString().padStart(2,'0')}`;
document.addEventListener("DOMContentLoaded",function(){
    document.querySelector(".prev").addEventListener("click",prevMonth);
    document.querySelector(".next").addEventListener("click",nextMonth);
    updateCalendar();
    selectToday();
});
function updateCalendar(){
    const year=cd.getFullYear();  // cd = currentDate
    const month=cd.getMonth();
    const daysInMonth=new Date(year, month+1, 0).getDate();
    document.getElementById("cym").textContent=`${year}.${(month+1).toString().padStart(2,'0')}`;
    const cb=document.getElementById("calendar");  // cb = calendarBody
    cb.innerHTML="";
    const startDay=new Date(year, month, 1).getDay();
    let day=1;
    let rowadd=0;
    let row=document.createElement("tr");
    for(let i=0;i<startDay;i++){
        let emptyCell=document.createElement("td");
        emptyCell.innerHTML="-";
        row.appendChild(emptyCell);
    }
    for(let i=startDay;i<7;i++){
        if(day>daysInMonth) break;
        let cell=createCalendarCell(day,year,month,i);
        row.appendChild(cell);
        day++;
    }
    cb.appendChild(row);
    rowadd++;
    while(day<=daysInMonth){
        row=document.createElement("tr");
        for(let i=0;i<7;i++){
            if(day>daysInMonth){
                let emptyCell=document.createElement("td");
                emptyCell.innerHTML="-";
                row.appendChild(emptyCell);
            }else{
                let cell=createCalendarCell(day,year,month,i);
                row.appendChild(cell);
                day++;
            }
        }
        cb.appendChild(row);
        rowadd++;
    }
    while(rowadd<6){
        row=document.createElement("tr");
        for(let i=0;i<7;i++){
            let emptyCell=document.createElement("td");
            emptyCell.innerHTML="-";
            row.appendChild(emptyCell);
        }
        cb.appendChild(row);
        rowadd++;
    }
}
function createCalendarCell(day,year,month,weekdayIndex){
    const cellDate=`${year}${(month+1).toString().padStart(2,'0')}${day.toString().padStart(2,'0')}`;
    let cell=document.createElement("td");
    cell.className="calendar_day";
    cell.textContent=day;
    cell.setAttribute("data-date",cellDate);
    if(weekdayIndex==0)cell.classList.add("redday");
    if(weekdayIndex==6)cell.classList.add("blueday");
    cell.addEventListener("click",()=>{  // 날짜 클릭 이벤트 추가
        if(cell.classList.contains("selected-date")){ return; }  // 단, 선택된 상태면 제외
        selectDate(cellDate);
    });
    return cell;
}
function prevMonth(){
    cd.setMonth(cd.getMonth()-1);
    updateCalendar();
}
function nextMonth(){
    cd.setMonth(cd.getMonth()+1);
    updateCalendar();
}
function selectToday(){
    const today=new Date();
    selectDate(`${today.getFullYear()}${(today.getMonth()+1).toString().padStart(2,'0')}${today.getDate().toString().padStart(2,'0')}`);
}
function selectDate(date){
    const calendarDays=document.querySelectorAll(".calendar_day")
    selectedDate=date;
    // 이전 선택 제거
    calendarDays.forEach(cell=>cell.classList.remove("selected-date"));
    const selectedCell=document.querySelector(`[data-date="${selectedDate}"]`);
    if(selectedCell){
        selectedCell.classList.add("selected-date");
    }
    loadFestivals(selectedDate);
}
async function loadFestivals(date){
    const festivalList=document.getElementById("festival-list");
    festivalList.innerHTML="<p>축제 정보 가져오는 중...</p>"
    if(!date){
        console.error("날짜미선택");
        return;
    }
    try{
        const response=await fetch(`/festival/data?date=${date}`);
        if(!response.ok){
            console.error("DB에서 데이터를 가져오지 못했음");
            return;
        }
        const data=await response.json();
        if(data.length==0){
            festivalList.innerHTML="<p>표시할 축제가 없습니다.</p>";
        }else{
            festivalList.innerHTML="";
            data.forEach(festival=>{
                const festivalDiv=document.createElement("div");
                festivalDiv.className="festival-card";
                festivalDiv.dataset.id=festival.festival_id;
                const imageUrl=festival.firstimage ? festival.firstimage : 'https://via.placeholder.com/400x200';
                festivalDiv.innerHTML=`
                    <img class="fes_img" src="${imageUrl}" alt="축제 이미지">
                    <h2 class="fes_name">${festival.title}</h2>
                    <p class="fes_start_date">시작일: ${festival.eventstartdate}</p>
                    <p class="fes_end_date">종료일: ${festival.eventenddate}</p>
                    <p class="fes_addr1">주소: ${festival.addr1}</p>
                `;
//                    ${festival.addr2 ? `<p><strong>장소: </strong>${festival.addr2}</p>` : ''}
//                    <button type="button" class="reserve-button">예약하기</button>
                festivalDiv.addEventListener("click",function(){  // 클릭 시 이벤트
                    window.location.href=`/festival/info/${festival.festival_id}`;
                });
                festivalList.appendChild(festivalDiv);
            });
        }
    }catch(error){
        console.error("DB 요청 중 오류 발생",error);
    }
}
function reserveFestival(festivalId,dodate){
    window.location.href=`/diary/reserve?festivalId=${festivalId}&godate=${godate}`;
}
