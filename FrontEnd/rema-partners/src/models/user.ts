export interface SocialLink {
    platform: string;
    url: string;
    icon?: string;
}

export interface User {
    readonly id: string
    username: string
    email: string
    password: string
    rol: {
        id: number;
        name: string;
    };
    description?: string;
    profileImage?: string;
    socialLinks?: SocialLink[];
    confirmPassword: string
}

export interface UserFormData {
    username: string
    email: string
    password: string
    confirmPassword: string
    description?: string
    profileImage?: string
    socialLinks?: SocialLink[]
}

export interface UserLogin {
    username: string
    password: string
}