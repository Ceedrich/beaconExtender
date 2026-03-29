{...}: {
  perSystem = {pkgs, ...}: {
    devShells.default = pkgs.mkShell {
      shellHook = ''
        export LD_LIBRARY_PATH="''${LD_LIBRARY_PATH}''${LD_LIBRARY_PATH:+:}${pkgs.libglvnd}/lib"
        export JAVA_HOME="${pkgs.jdk}"
      '';
    };
  };
}
