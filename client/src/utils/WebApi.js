

import axios from "axios";

//TODO: resolve it from env
const advaya_analytics = "http://localhost:8081";
export const advaya_attendance = "http://localhost:8080";
const advaya_takehomeration = "http://35.200.198.196";
const advaya_hotmeal = "http://35.200.221.58";

const createQueryParams = (code, date) => {
    const codeP = code && `code=${code}`;
    const dateP = date && `date=${date}`;

    return [codeP, dateP].filter(p => p && p).join("&");
}

export const fetchAMRLog = (code, date) => axios.get(`${advaya_attendance}/amr?${createQueryParams(code, date)}`)

export const fetchTHRLog = (code, date) => axios.get(`${advaya_takehomeration}/thr?${createQueryParams(code, date)}`)

export const fetchHMRLog = (code, date) => axios.get(`${advaya_hotmeal}/hmr?${createQueryParams(code, date)}`)

export const fetchBeneficiaryImage = imageUrl => axios.get(imageUrl)

export const fetchAanganwadis = () => axios.get(`${advaya_analytics}/admin/aanganwadis`)