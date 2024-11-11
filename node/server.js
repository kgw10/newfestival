/* server.js */
const express = require("express");
const axios = require("axios");
const cors = require("cors");
const app = express();
const PORT = 3000;

// API 키를 환경 변수 또는 직접 문자열로 입력
const API_KEY = "%2BYmKMpujVU9nNBSYycV1idRn%2B%2BC7dAOkd%2BUNh7EOLtpg1k2sobgqkdyx6wndSZK5uddFpLhYNGHL%2BzzqoY6vcQ%3D%3D";

app.use(cors());

app.get("/api/festivals", async (req, res) => {
    const today = new Date().toISOString().split("T")[0].replace(/-/g, "");
    const url = `https://api.visitkorea.or.kr/openapi/service/rest/KorService/searchFestival`;

    try {
        const response = await axios.get(url, {
            params: {
                serviceKey: API_KEY,
                numOfRows: 10,
                pageNo: 1,
                MobileOS: "ETC",
                MobileApp: "TestApp",
                arrange: "A",
                eventStartDate: today
            }
        });
        res.json(response.data.response.body.items.item);  // 클라이언트에 API 결과 반환
    } catch (error) {
        console.error("API 요청 실패:", error);
        res.status(500).json({ message: "서버 오류" });
    }
});

app.listen(PORT, () => {
    console.log(`서버가 http://localhost:${PORT} 에서 실행 중입니다.`);
});
