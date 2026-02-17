export interface JobOpeningBase {
    jobTitle: string;
    jobLocation: string;
    jobDescription: string;
    noOfOpening: number;
    experience: number;
    jobType: "FULLTIME" | "PARTTIME" | "INTERNSHIP";
}

export interface CreateJobOpening extends JobOpeningBase {
    file: FileList;
}

export interface GetJobOpening extends JobOpeningBase{
    jobOpeningId: string;
}

export interface PageResponse<T> {
    content: T[];
    totalPages: number;
    number: number;
    size: number;
    first: boolean;
    last: boolean;
}