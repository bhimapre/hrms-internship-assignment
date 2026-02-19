export interface JobReferralBase{
    name: string;
    email: string;
    phoneNumber: string;
}

export interface AddJobReferral extends JobReferralBase{
    shortNote?: string;
    file: FileList;
}

export interface FetchAllJobReferrals extends JobReferralBase{
    shortNote?: string;
    jobReferralId: string;
    jobReferralStatus: string;
}

export interface UpdateJobReferral extends JobReferralBase{
    shortNote?: string;
    jobReferralStatus: string;
}

export interface PageResponse<T> {
    content: T[];
    totalPages: number;
    number: number;
    size: number;
    first: boolean;
    last: boolean;
}