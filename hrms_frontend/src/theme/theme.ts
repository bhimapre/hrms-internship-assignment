import { createTheme } from "@mui/material/styles";

export const theme = createTheme({
  palette: {
    mode: "dark",
    primary: {
      main: "#7c3aed",
    },
    background: {
      default: "#0a0a0a",
      paper: "#171717", 
    },
    text: {
      primary: "#ffffff",
      secondary: "#cbd5e1",
    },
  },
  shape: {
    borderRadius: 12,
  },
  typography: {
    fontFamily: "Inter, system-ui, sans-serif",
  },
});