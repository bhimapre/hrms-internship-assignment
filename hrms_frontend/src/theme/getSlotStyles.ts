export const getSlotStyles = (
    status: string,
    isSelected: boolean
) => {
    if (isSelected) {
        return {
            bgcolor: "primary.main",
            color: "#fff",
        };
    }

    switch (status) {
        case "PAST":
            return {
                bgcolor: "#424242",
                color: "#aaa",
                cursor: "not-allowed",
            };

        case "BOOKED":
            return {
                bgcolor: "#d32f2f",
                color: "#fff",
                cursor: "not-allowed",
            };

        case "AVAILABLE":
            return {
                bgcolor: "#2e7d32",
                color: "#fff",
            };
    }
}