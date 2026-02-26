library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;

entity dice is 
    port(
        clk     : in std_logic;
        clr     : in std_logic;
        roll    : in std_logic;
        fake    : in std_logic;
        seg     : out std_logic_vector(6 downto 0);
        dp      : out std_logic;
        an      : out std_logic_vector(3 downto 0)
    );
end entity;

architecture arch of dice is
    signal saved_val : unsigned(2 downto 0) := to_unsigned(1, 3);
    signal roll_d    : std_logic := '0';
    signal r         : unsigned(2 downto 0) := (others => '0');

    type ROM_7seg is array (0 to 7) of std_logic_vector(6 downto 0);
    constant mem : ROM_7seg := (
        "1000000", 
        "1111001",
        "0100100", 
        "0110000", 
        "0011001", 
        "0010010", 
        "0000010",
        "1111111"  
    );

begin

    process(clk, clr) 
    begin
        if clr = '1' then
            saved_val <= to_unsigned(1, 3);
            roll_d    <= '0';
            r         <= (others => '0');

        elsif rising_edge(clk) then
            roll_d <= roll;

        if roll = '1' then
            if fake = '1' then
                r <= r + 1;
            else
                if r = "110" then
                    r <= "001";
                else
                    r <= r + 1;
                end if;
            end if;
        end if;

            if (roll = '0' and roll_d = '1') then
                if fake = '1' then
                    case r is
                        when "000"  => saved_val <= "001";
                        when "001"  => saved_val <= "010";
                        when "010"  => saved_val <= "011";
                        when "011"  => saved_val <= "100";
                        when "100"  => saved_val <= "101";
                        when others => saved_val <= "110";
                    end case;
                else
                    saved_val <= r;
                end if;
            end if;
        end if;
    end process;

    seg <= mem(to_integer(saved_val));  
    dp  <= '1';
    an  <= "1110";

end architecture;